package com.trackr.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.trackr.TestUtil;
import com.trackr.service.dto.NewsletterDTO;
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
public class NewsletterResourceTest {

    private static final TypeRef<NewsletterDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<NewsletterDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final String DEFAULT_ORGANISATION = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION = "BBBBBBBBBB";



    String adminToken;

    NewsletterDTO newsletterDTO;


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
    public static NewsletterDTO createEntity() {
        var newsletterDTO = new NewsletterDTO();
        newsletterDTO.subject = DEFAULT_SUBJECT;
        newsletterDTO.body = DEFAULT_BODY;
        newsletterDTO.createdDate = DEFAULT_CREATED_DATE;
        newsletterDTO.organisation = DEFAULT_ORGANISATION;
        return newsletterDTO;
    }

    @BeforeEach
    public void initTest() {
        newsletterDTO = createEntity();
    }

    @Test
    public void createNewsletter() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Newsletter
        newsletterDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(newsletterDTO)
            .when()
            .post("/api/newsletters")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the Newsletter in the database
        var newsletterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(newsletterDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testNewsletterDTO = newsletterDTOList.stream().filter(it -> newsletterDTO.id.equals(it.id)).findFirst().get();
        assertThat(testNewsletterDTO.subject).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testNewsletterDTO.body).isEqualTo(DEFAULT_BODY);
        assertThat(testNewsletterDTO.createdDate).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testNewsletterDTO.organisation).isEqualTo(DEFAULT_ORGANISATION);
    }

    @Test
    public void createNewsletterWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Newsletter with an existing ID
        newsletterDTO.id = new ObjectId("1");

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(newsletterDTO)
            .when()
            .post("/api/newsletters")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Newsletter in the database
        var newsletterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(newsletterDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkSubjectIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        newsletterDTO.subject = null;

        // Create the Newsletter, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(newsletterDTO)
            .when()
            .post("/api/newsletters")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Newsletter in the database
        var newsletterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(newsletterDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkBodyIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        newsletterDTO.body = null;

        // Create the Newsletter, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(newsletterDTO)
            .when()
            .post("/api/newsletters")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Newsletter in the database
        var newsletterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(newsletterDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkCreatedDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        newsletterDTO.createdDate = null;

        // Create the Newsletter, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(newsletterDTO)
            .when()
            .post("/api/newsletters")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Newsletter in the database
        var newsletterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(newsletterDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkOrganisationIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        newsletterDTO.organisation = null;

        // Create the Newsletter, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(newsletterDTO)
            .when()
            .post("/api/newsletters")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Newsletter in the database
        var newsletterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(newsletterDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateNewsletter() {
        // Initialize the database
        newsletterDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(newsletterDTO)
            .when()
            .post("/api/newsletters")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the newsletter
        var updatedNewsletterDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters/{id}", newsletterDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the newsletter
        updatedNewsletterDTO.subject = UPDATED_SUBJECT;
        updatedNewsletterDTO.body = UPDATED_BODY;
        updatedNewsletterDTO.createdDate = UPDATED_CREATED_DATE;
        updatedNewsletterDTO.organisation = UPDATED_ORGANISATION;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedNewsletterDTO)
            .when()
            .put("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Newsletter in the database
        var newsletterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(newsletterDTOList).hasSize(databaseSizeBeforeUpdate);
        var testNewsletterDTO = newsletterDTOList.stream().filter(it -> updatedNewsletterDTO.id.equals(it.id)).findFirst().get();
        assertThat(testNewsletterDTO.subject).isEqualTo(UPDATED_SUBJECT);
        assertThat(testNewsletterDTO.body).isEqualTo(UPDATED_BODY);
        assertThat(testNewsletterDTO.createdDate).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testNewsletterDTO.organisation).isEqualTo(UPDATED_ORGANISATION);
    }

    @Test
    public void updateNonExistingNewsletter() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
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
            .body(newsletterDTO)
            .when()
            .put("/api/newsletters")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Newsletter in the database
        var newsletterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(newsletterDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteNewsletter() {
        // Initialize the database
        newsletterDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(newsletterDTO)
            .when()
            .post("/api/newsletters")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the newsletter
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/newsletters/{id}", newsletterDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var newsletterDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(newsletterDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllNewsletters() {
        // Initialize the database
        newsletterDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(newsletterDTO)
            .when()
            .post("/api/newsletters")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the newsletterList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(newsletterDTO.id))
            .body("subject", hasItem(DEFAULT_SUBJECT))            .body("body", hasItem(DEFAULT_BODY))            .body("createdDate", hasItem(TestUtil.formatDateTime(DEFAULT_CREATED_DATE)))            .body("organisation", hasItem(DEFAULT_ORGANISATION));
    }

    @Test
    public void getNewsletter() {
        // Initialize the database
        newsletterDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(newsletterDTO)
            .when()
            .post("/api/newsletters")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the newsletter
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/newsletters/{id}", newsletterDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the newsletter
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters/{id}", newsletterDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(newsletterDTO.id))

                .body("subject", is(DEFAULT_SUBJECT))
                .body("body", is(DEFAULT_BODY))
                .body("createdDate", is(TestUtil.formatDateTime(DEFAULT_CREATED_DATE)))
                .body("organisation", is(DEFAULT_ORGANISATION));
    }

    @Test
    public void getNonExistingNewsletter() {
        // Get the newsletter
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/newsletters/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
