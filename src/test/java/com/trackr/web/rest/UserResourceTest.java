package com.trackr.web.rest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.trackr.TestUtil;
import com.trackr.domain.Authority;
import com.trackr.domain.User;
import com.trackr.security.AuthoritiesConstants;
import com.trackr.service.dto.UserDTO;
import com.trackr.service.mapper.UserMapper;
import com.trackr.web.rest.vm.ManagedUserVM;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import java.time.Instant;
import java.util.Set;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.*;

@QuarkusTest
public class UserResourceTest {
    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String UPDATED_LOGIN = "jhipster";

    private static final String DEFAULT_PASSWORD = "passjohndoe";
    private static final String UPDATED_PASSWORD = "passjhipster";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String UPDATED_EMAIL = "jhipster@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String UPDATED_FIRSTNAME = "jhipsterFirstName";

    private static final String DEFAULT_LASTNAME = "doe";
    private static final String UPDATED_LASTNAME = "jhipsterLastName";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String UPDATED_IMAGEURL = "http://placehold.it/40x40";

    private static final String DEFAULT_LANGKEY = "en";
    private static final String UPDATED_LANGKEY = "fr";

    String adminToken;

    ManagedUserVM managedUserVM;


    @ConfigProperty(name = "application.name")
    String applicationName;

    @BeforeAll
    static void jsonMapper() {
        RestAssured.config =
            RestAssured.config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(TestUtil.jsonbObjectMapper()));
    }

    @BeforeEach
    public void authenticateAdmin() {
        this.adminToken = TestUtil.getAdminToken();
    }


    @BeforeEach
    public void initTest() {
        managedUserVM = new ManagedUserVM();
        managedUserVM.login = DEFAULT_LOGIN;
        managedUserVM.password = DEFAULT_PASSWORD;
        managedUserVM.firstName = DEFAULT_FIRSTNAME;
        managedUserVM.lastName = DEFAULT_LASTNAME;
        managedUserVM.email = DEFAULT_EMAIL;
        managedUserVM.activated = true;
        managedUserVM.imageUrl = DEFAULT_IMAGEURL;
        managedUserVM.langKey = DEFAULT_LANGKEY;
        managedUserVM.authorities = Set.of(AuthoritiesConstants.USER);
    }

    @Test
    public void createUser() {
        // Create the User
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(managedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(CREATED.getStatusCode());

        var testUser = get("/api/users/{login}", managedUserVM.login).then().extract().body().as(User.class);
        // Validate the User in the database
        assertThat(testUser.login).isEqualTo(DEFAULT_LOGIN);
        assertThat(testUser.firstName).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testUser.lastName).isEqualTo(DEFAULT_LASTNAME);
        assertThat(testUser.email).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUser.imageUrl).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(testUser.langKey).isEqualTo(DEFAULT_LANGKEY);
    }

    @Test
    public void createUserWithExistingId() {
        managedUserVM.id = new ObjectId("1");
        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(managedUserVM)
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, APPLICATION_JSON)
            .when()
            .post("/api/users")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode())
            .header("X-" + applicationName + "-error", is("error.idexists"))
            .header("X-" + applicationName + "-params", is("userManagement"))
            .contentType("application/problem+json")
            .body("title", is("A new user cannot already have an ID"))
            .body("entityName", is("userManagement"))
            .body("errorKey", is("idexists"))
            .body("message", is("error.idexists"))
            .body("params", is("userManagement"));
    }

    @Test
    @Transactional
    public void createUserWithExistingLogin() throws Exception {
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(managedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(CREATED.getStatusCode());

        var otherManagedUserVM = new ManagedUserVM();
        otherManagedUserVM.login = DEFAULT_LOGIN; // this login should already be used
        otherManagedUserVM.password = DEFAULT_PASSWORD;
        otherManagedUserVM.firstName = DEFAULT_FIRSTNAME;
        otherManagedUserVM.lastName = DEFAULT_LASTNAME;
        otherManagedUserVM.email = "anothermail@localhost";
        otherManagedUserVM.activated = true;
        otherManagedUserVM.imageUrl = DEFAULT_IMAGEURL;
        otherManagedUserVM.langKey = DEFAULT_LANGKEY;
        otherManagedUserVM.authorities = Set.of(AuthoritiesConstants.USER);

        // Create the User
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(otherManagedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode())
            .header("X-" + applicationName + "-error", is("error.userexists"))
            .header("X-" + applicationName + "-params", is("userManagement"))
            .contentType("application/problem+json")
            .body("title", is("Login name already used!"))
            .body("entityName", is("userManagement"))
            .body("errorKey", is("userexists"))
            .body("message", is("error.userexists"))
            .body("params", is("userManagement"));
    }

    @Test
    public void createUserWithExistingEmail() throws Exception {
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(managedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(CREATED.getStatusCode());

        var otherManagedUserVM = new ManagedUserVM();
        otherManagedUserVM.login = "anotherlogin";
        otherManagedUserVM.password = DEFAULT_PASSWORD;
        otherManagedUserVM.firstName = DEFAULT_FIRSTNAME;
        otherManagedUserVM.lastName = DEFAULT_LASTNAME;
        otherManagedUserVM.email = DEFAULT_EMAIL; // this email should already be used
        otherManagedUserVM.activated = true;
        otherManagedUserVM.imageUrl = DEFAULT_IMAGEURL;
        otherManagedUserVM.langKey = DEFAULT_LANGKEY;
        otherManagedUserVM.authorities = Set.of(AuthoritiesConstants.USER);

        // Create the User
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(managedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode())
            .header("X-" + applicationName + "-error", is("error.userexists"))
            .header("X-" + applicationName + "-params", is("userManagement"))
            .contentType("application/problem+json")
            .body("title", is("Login name already used!"))
            .body("entityName", is("userManagement"))
            .body("errorKey", is("userexists"))
            .body("message", is("error.userexists"))
            .body("params", is("userManagement"));
    }

    @Test
    public void getAllUsers() throws Exception {
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(managedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(CREATED.getStatusCode());

        // Get all the users
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/users?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("login", hasItem(DEFAULT_LOGIN))
            .body("firstName", hasItem(DEFAULT_FIRSTNAME))
            .body("lastName", hasItem(DEFAULT_LASTNAME))
            .body("email", hasItem(DEFAULT_EMAIL))
            .body("imageUrl", hasItem(DEFAULT_IMAGEURL))
            .body("langKey", hasItem(DEFAULT_LANGKEY))
            .body("login", hasItem(DEFAULT_LOGIN));
    }

    @Test
    public void getUser() throws Exception {
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(managedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(CREATED.getStatusCode());

        // Get the user
        get("/api/users/{login}", managedUserVM.login)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("login", is(managedUserVM.login))
            .body("firstName", is(DEFAULT_FIRSTNAME))
            .body("lastName", is(DEFAULT_LASTNAME))
            .body("email", is(DEFAULT_EMAIL))
            .body("imageUrl", is(DEFAULT_IMAGEURL))
            .body("langKey", is(DEFAULT_LANGKEY))
            .body("login", is(DEFAULT_LOGIN));
    }

    @Test
    public void getNonExistingUser() {
        given().accept(APPLICATION_JSON).when().get("/api/users/unknown").then().statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    public void updateUser() throws Exception {
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(managedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(CREATED.getStatusCode());

        // Update the user
        var updatedUser = get("/api/users/{login}", managedUserVM.login).then().extract().body().as(User.class);

        ManagedUserVM updatedManagedUserVM = new ManagedUserVM();
        updatedManagedUserVM.id = updatedUser.id;
        updatedManagedUserVM.login = updatedUser.login;
        updatedManagedUserVM.password = UPDATED_PASSWORD;
        updatedManagedUserVM.firstName = UPDATED_FIRSTNAME;
        updatedManagedUserVM.lastName = UPDATED_LASTNAME;
        updatedManagedUserVM.email = UPDATED_EMAIL;
        updatedManagedUserVM.activated = updatedUser.activated;
        updatedManagedUserVM.imageUrl = UPDATED_IMAGEURL;
        updatedManagedUserVM.langKey = UPDATED_LANGKEY;
        updatedManagedUserVM.createdBy = updatedUser.createdBy;
        updatedManagedUserVM.createdDate = updatedUser.createdDate;
        updatedManagedUserVM.lastModifiedBy = updatedUser.lastModifiedBy;
        updatedManagedUserVM.lastModifiedDate = updatedUser.lastModifiedDate;
        updatedManagedUserVM.authorities = Set.of(AuthoritiesConstants.USER);

        // Update the User
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedManagedUserVM)
            .when()
            .put("/api/users")
            .then()
            .statusCode(OK.getStatusCode());

        User testUser = get("/api/users/{login}", managedUserVM.login).then().extract().body().as(User.class);
        assertThat(testUser.firstName).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testUser.lastName).isEqualTo(UPDATED_LASTNAME);
        assertThat(testUser.email).isEqualTo(UPDATED_EMAIL);
        assertThat(testUser.imageUrl).isEqualTo(UPDATED_IMAGEURL);
        assertThat(testUser.langKey).isEqualTo(UPDATED_LANGKEY);
    }

    @Test
    public void updateUserLogin() throws Exception {
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(managedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(CREATED.getStatusCode());

        // Update the user
        var updatedUser = get("/api/users/{login}", managedUserVM.login).then().extract().body().as(User.class);

        ManagedUserVM updatedManagedUserVM = new ManagedUserVM();
        updatedManagedUserVM.id = updatedUser.id;
        updatedManagedUserVM.login = UPDATED_LOGIN;
        updatedManagedUserVM.password = UPDATED_PASSWORD;
        updatedManagedUserVM.firstName = UPDATED_FIRSTNAME;
        updatedManagedUserVM.lastName = UPDATED_LASTNAME;
        updatedManagedUserVM.email = UPDATED_EMAIL;
        updatedManagedUserVM.activated = updatedUser.activated;
        updatedManagedUserVM.imageUrl = UPDATED_IMAGEURL;
        updatedManagedUserVM.langKey = UPDATED_LANGKEY;
        updatedManagedUserVM.createdBy = updatedUser.createdBy;
        updatedManagedUserVM.createdDate = updatedUser.createdDate;
        updatedManagedUserVM.lastModifiedBy = updatedUser.lastModifiedBy;
        updatedManagedUserVM.lastModifiedDate = updatedUser.lastModifiedDate;
        updatedManagedUserVM.authorities = Set.of(AuthoritiesConstants.USER);

        // Update the User
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedManagedUserVM)
            .when()
            .put("/api/users")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the User in the database
        var testUser = get("/api/users/{login}", updatedManagedUserVM.login).then().extract().body().as(User.class);
        assertThat(testUser.login).isEqualTo(UPDATED_LOGIN);
        assertThat(testUser.firstName).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testUser.lastName).isEqualTo(UPDATED_LASTNAME);
        assertThat(testUser.email).isEqualTo(UPDATED_EMAIL);
        assertThat(testUser.imageUrl).isEqualTo(UPDATED_IMAGEURL);
        assertThat(testUser.langKey).isEqualTo(UPDATED_LANGKEY);
    }

    @Test
    @Transactional
    public void updateUserExistingEmail() throws Exception {
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(managedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(CREATED.getStatusCode());

        var otherManagedUserVM = new ManagedUserVM();
        otherManagedUserVM.login = "jhipster";
        otherManagedUserVM.password = RandomStringUtils.random(60);
        otherManagedUserVM.activated = true;
        otherManagedUserVM.email = "jhipster@localhost";
        otherManagedUserVM.firstName = "java";
        otherManagedUserVM.lastName = "hipster";
        otherManagedUserVM.imageUrl = "";
        otherManagedUserVM.langKey = "en";

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(otherManagedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(CREATED.getStatusCode());

        // Update the user
        var updatedUser = get("/api/users/{login}", managedUserVM.login).then().extract().body().as(User.class);

        var updatedManagedUserVM = new ManagedUserVM();
        updatedManagedUserVM.id = updatedUser.id;
        updatedManagedUserVM.login = updatedUser.login;
        updatedManagedUserVM.password = updatedUser.password;
        updatedManagedUserVM.firstName = updatedUser.firstName;
        updatedManagedUserVM.lastName = updatedUser.lastName;
        updatedManagedUserVM.email = otherManagedUserVM.email; // this email should already be used by anotherManagedUserVM
        updatedManagedUserVM.activated = updatedUser.activated;
        updatedManagedUserVM.imageUrl = updatedUser.imageUrl;
        updatedManagedUserVM.langKey = updatedUser.langKey;
        updatedManagedUserVM.createdBy = updatedUser.createdBy;
        updatedManagedUserVM.createdDate = updatedUser.createdDate;
        updatedManagedUserVM.lastModifiedBy = updatedUser.lastModifiedBy;
        updatedManagedUserVM.lastModifiedDate = updatedUser.lastModifiedDate;
        updatedManagedUserVM.authorities = Set.of(AuthoritiesConstants.USER);

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedManagedUserVM)
            .when()
            .put("/api/users")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode())
            .header("X-" + applicationName + "-error", is("error.emailexists"))
            .header("X-" + applicationName + "-params", is("userManagement"))
            .contentType("application/problem+json")
            .body("title", is("Email is already in use!"))
            .body("entityName", is("userManagement"))
            .body("errorKey", is("emailexists"))
            .body("message", is("error.emailexists"))
            .body("params", is("userManagement"));
    }

    @Test
    public void updateUserExistingLogin() throws Exception {
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(managedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(CREATED.getStatusCode());

        var otherManagedUserVM = new ManagedUserVM();
        otherManagedUserVM.login = "jhipster";
        otherManagedUserVM.password = RandomStringUtils.random(60);
        otherManagedUserVM.activated = true;
        otherManagedUserVM.email = "jhipster@localhost";
        otherManagedUserVM.firstName = "java";
        otherManagedUserVM.lastName = "hipster";
        otherManagedUserVM.imageUrl = "";
        otherManagedUserVM.langKey = "en";

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(otherManagedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(CREATED.getStatusCode());

        // Update the user
        User updatedUser = get("/api/users/{login}", managedUserVM.login).then().extract().body().as(User.class);

        var updatedManagedUserVM = new ManagedUserVM();
        updatedManagedUserVM.id = updatedUser.id;
        updatedManagedUserVM.login = otherManagedUserVM.login; // this login should already be used by anotherManagedUserVM
        updatedManagedUserVM.password = updatedUser.password;
        updatedManagedUserVM.firstName = updatedUser.firstName;
        updatedManagedUserVM.lastName = updatedUser.lastName;
        updatedManagedUserVM.email = updatedUser.email;
        updatedManagedUserVM.activated = updatedUser.activated;
        updatedManagedUserVM.imageUrl = updatedUser.imageUrl;
        updatedManagedUserVM.langKey = updatedUser.langKey;
        updatedManagedUserVM.createdBy = updatedUser.createdBy;
        updatedManagedUserVM.createdDate = updatedUser.createdDate;
        updatedManagedUserVM.lastModifiedBy = updatedUser.lastModifiedBy;
        updatedManagedUserVM.lastModifiedDate = updatedUser.lastModifiedDate;
        updatedManagedUserVM.authorities = Set.of(AuthoritiesConstants.USER);

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedManagedUserVM)
            .when()
            .put("/api/users")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode())
            .header("X-" + applicationName + "-error", is("error.userexists"))
            .header("X-" + applicationName + "-params", is("userManagement"))
            .contentType("application/problem+json")
            .body("title", is("Login name already used!"))
            .body("entityName", is("userManagement"))
            .body("errorKey", is("userexists"))
            .body("message", is("error.userexists"))
            .body("params", is("userManagement"));
    }

    @Test
    public void deleteUser() throws Exception {
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(managedUserVM)
            .when()
            .post("/api/users")
            .then()
            .statusCode(CREATED.getStatusCode());

        // Delete the user
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/users/{login}", managedUserVM.login)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the user has been removed database
        get("/api/users/{login}", managedUserVM.login).then().statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    public void getAllAuthorities() {
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/users/authorities")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("$", hasSize(greaterThan(0)))
            .body("$", hasItems(AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN));
    }

    @Test
    public void testUserDTOtoUser() {
        var userMapper = new UserMapper();
        var userDTO = new UserDTO();
        userDTO.id = new ObjectId("1");
        userDTO.login = DEFAULT_LOGIN;
        userDTO.firstName = DEFAULT_FIRSTNAME;
        userDTO.lastName = DEFAULT_LASTNAME;
        userDTO.email = DEFAULT_EMAIL;
        userDTO.activated = true;
        userDTO.imageUrl = DEFAULT_IMAGEURL;
        userDTO.langKey = DEFAULT_LANGKEY;
        userDTO.createdBy = DEFAULT_LOGIN;
        userDTO.lastModifiedBy = DEFAULT_LOGIN;
        userDTO.authorities = Set.of(AuthoritiesConstants.USER);

        User user = userMapper.userDTOToUser(userDTO);
        assertThat(user.id).isEqualTo("1");
        assertThat(user.login).isEqualTo(DEFAULT_LOGIN);
        assertThat(user.firstName).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(user.lastName).isEqualTo(DEFAULT_LASTNAME);
        assertThat(user.email).isEqualTo(DEFAULT_EMAIL);
        assertThat(user.activated).isEqualTo(true);
        assertThat(user.imageUrl).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(user.langKey).isEqualTo(DEFAULT_LANGKEY);
        assertThat(user.createdBy).isBlank();
        assertThat(user.createdDate).isNotNull();
        assertThat(user.lastModifiedBy).isBlank();
        assertThat(user.lastModifiedDate).isNotNull();
        assertThat(user.authorities).extracting("name").containsExactly(AuthoritiesConstants.USER);
    }

    @Test
    public void testUserToUserDTO() {
        var userMapper = new UserMapper();
        var user = new User();
        user.id = new ObjectId("1");
        user.login = DEFAULT_LOGIN;
        user.password = DEFAULT_PASSWORD;
        user.firstName = DEFAULT_FIRSTNAME;
        user.lastName = DEFAULT_LASTNAME;
        user.email = DEFAULT_EMAIL;
        user.activated = true;
        user.imageUrl = DEFAULT_IMAGEURL;
        user.langKey = DEFAULT_LANGKEY;
        user.createdBy = DEFAULT_LOGIN;
        user.createdDate = Instant.now();
        user.lastModifiedBy = DEFAULT_LOGIN;
        user.lastModifiedDate = Instant.now();
        var authority = new Authority();
        authority.name = AuthoritiesConstants.USER;
        user.authorities = Set.of(authority);

        UserDTO userDTO = userMapper.userToUserDTO(user);

        assertThat(userDTO.id).isEqualTo("1");
        assertThat(userDTO.login).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.firstName).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(userDTO.lastName).isEqualTo(DEFAULT_LASTNAME);
        assertThat(userDTO.email).isEqualTo(DEFAULT_EMAIL);
        assertThat(userDTO.activated).isEqualTo(true);
        assertThat(userDTO.imageUrl).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(userDTO.langKey).isEqualTo(DEFAULT_LANGKEY);
        assertThat(userDTO.createdBy).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.createdDate).isEqualTo(user.createdDate);
        assertThat(userDTO.lastModifiedBy).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.lastModifiedDate).isEqualTo(user.lastModifiedDate);
        assertThat(userDTO.authorities).containsExactly(AuthoritiesConstants.USER);
        assertThat(userDTO.toString()).isNotNull();
    }
}
