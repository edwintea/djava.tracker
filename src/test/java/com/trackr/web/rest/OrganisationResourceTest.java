package com.trackr.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.trackr.TestUtil;
import com.trackr.service.dto.OrganisationDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

import java.util.List;
    import java.time.Instant;
import java.time.temporal.ChronoUnit;

@QuarkusTest
public class OrganisationResourceTest {

    private static final TypeRef<OrganisationDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<OrganisationDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_SINCE_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_SINCE_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final String DEFAULT_JOIN_CODE = "AAAAAAAAAA";
    private static final String UPDATED_JOIN_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_REG = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_REG = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";



    String adminToken;

    OrganisationDTO organisationDTO;


    @BeforeAll
    static void jsonMapper() {
        RestAssured.config =
            RestAssured.config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(TestUtil.jsonbObjectMapper()));
    }

    @BeforeEach
    public void authenticateAdmin() {
        this.adminToken = TestUtil.getAdminToken();
    }




    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganisationDTO createEntity() {
        var organisationDTO = new OrganisationDTO();
        organisationDTO.name = DEFAULT_NAME;
        organisationDTO.sinceDate = DEFAULT_SINCE_DATE;
        organisationDTO.joinCode = DEFAULT_JOIN_CODE;
        organisationDTO.companyReg = DEFAULT_COMPANY_REG;
        organisationDTO.address = DEFAULT_ADDRESS;
        return organisationDTO;
    }

    @BeforeEach
    public void initTest() {
        organisationDTO = createEntity();
    }

    @Test
    public void createOrganisation() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Organisation
        organisationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organisationDTO)
            .when()
            .post("/api/organisations")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the Organisation in the database
        var organisationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(organisationDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testOrganisationDTO = organisationDTOList.stream().filter(it -> organisationDTO.id.equals(it.id)).findFirst().get();
        assertThat(testOrganisationDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganisationDTO.sinceDate).isEqualTo(DEFAULT_SINCE_DATE);
        assertThat(testOrganisationDTO.joinCode).isEqualTo(DEFAULT_JOIN_CODE);
        assertThat(testOrganisationDTO.companyReg).isEqualTo(DEFAULT_COMPANY_REG);
        assertThat(testOrganisationDTO.address).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    public void createOrganisationWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Organisation with an existing ID
        organisationDTO.id = new ObjectId("1");

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organisationDTO)
            .when()
            .post("/api/organisations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organisation in the database
        var organisationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(organisationDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        organisationDTO.name = null;

        // Create the Organisation, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organisationDTO)
            .when()
            .post("/api/organisations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organisation in the database
        var organisationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(organisationDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkSinceDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        organisationDTO.sinceDate = null;

        // Create the Organisation, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organisationDTO)
            .when()
            .post("/api/organisations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organisation in the database
        var organisationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(organisationDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkJoinCodeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        organisationDTO.joinCode = null;

        // Create the Organisation, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organisationDTO)
            .when()
            .post("/api/organisations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organisation in the database
        var organisationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(organisationDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkCompanyRegIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        organisationDTO.companyReg = null;

        // Create the Organisation, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organisationDTO)
            .when()
            .post("/api/organisations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organisation in the database
        var organisationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(organisationDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateOrganisation() {
        // Initialize the database
        organisationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organisationDTO)
            .when()
            .post("/api/organisations")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the organisation
        var updatedOrganisationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations/{id}", organisationDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the organisation
        updatedOrganisationDTO.name = UPDATED_NAME;
        updatedOrganisationDTO.sinceDate = UPDATED_SINCE_DATE;
        updatedOrganisationDTO.joinCode = UPDATED_JOIN_CODE;
        updatedOrganisationDTO.companyReg = UPDATED_COMPANY_REG;
        updatedOrganisationDTO.address = UPDATED_ADDRESS;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedOrganisationDTO)
            .when()
            .put("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Organisation in the database
        var organisationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(organisationDTOList).hasSize(databaseSizeBeforeUpdate);
        var testOrganisationDTO = organisationDTOList.stream().filter(it -> updatedOrganisationDTO.id.equals(it.id)).findFirst().get();
        assertThat(testOrganisationDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testOrganisationDTO.sinceDate).isEqualTo(UPDATED_SINCE_DATE);
        assertThat(testOrganisationDTO.joinCode).isEqualTo(UPDATED_JOIN_CODE);
        assertThat(testOrganisationDTO.companyReg).isEqualTo(UPDATED_COMPANY_REG);
        assertThat(testOrganisationDTO.address).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    public void updateNonExistingOrganisation() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organisationDTO)
            .when()
            .put("/api/organisations")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Organisation in the database
        var organisationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(organisationDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteOrganisation() {
        // Initialize the database
        organisationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organisationDTO)
            .when()
            .post("/api/organisations")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the organisation
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/organisations/{id}", organisationDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var organisationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(organisationDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllOrganisations() {
        // Initialize the database
        organisationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organisationDTO)
            .when()
            .post("/api/organisations")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the organisationList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(organisationDTO.id))
            .body("name", hasItem(DEFAULT_NAME))            .body("sinceDate", hasItem(TestUtil.formatDateTime(DEFAULT_SINCE_DATE)))            .body("joinCode", hasItem(DEFAULT_JOIN_CODE))            .body("companyReg", hasItem(DEFAULT_COMPANY_REG))            .body("address", hasItem(DEFAULT_ADDRESS));
    }

    @Test
    public void getOrganisation() {
        // Initialize the database
        organisationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(organisationDTO)
            .when()
            .post("/api/organisations")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the organisation
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/organisations/{id}", organisationDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the organisation
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations/{id}", organisationDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(organisationDTO.id))

                .body("name", is(DEFAULT_NAME))
                .body("sinceDate", is(TestUtil.formatDateTime(DEFAULT_SINCE_DATE)))
                .body("joinCode", is(DEFAULT_JOIN_CODE))
                .body("companyReg", is(DEFAULT_COMPANY_REG))
                .body("address", is(DEFAULT_ADDRESS));
    }

    @Test
    public void getNonExistingOrganisation() {
        // Get the organisation
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/organisations/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
