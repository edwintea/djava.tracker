package com.trackr.web.rest;

import static com.trackr.domain.enumeration.AnnualLeaveStatus.APPROVED;
import static com.trackr.domain.enumeration.AnnualLeaveStatus.REJECTED;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.trackr.TestUtil;
import com.trackr.domain.enumeration.AnnualLeaveStatus;
import com.trackr.service.dto.AnnualLeaveDTO;
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
public class AnnualLeaveResourceTest {

    private static final TypeRef<AnnualLeaveDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<AnnualLeaveDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_FROM_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final Instant DEFAULT_TO_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_TO_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final AnnualLeaveStatus DEFAULT_STATUS = APPROVED;
    private static final AnnualLeaveStatus UPDATED_STATUS = REJECTED;

    private static final String DEFAULT_ORGANISATION = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION = "BBBBBBBBBB";



    String adminToken;

    AnnualLeaveDTO annualLeaveDTO;


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
    public static AnnualLeaveDTO createEntity() {
        var annualLeaveDTO = new AnnualLeaveDTO();
        annualLeaveDTO.user = DEFAULT_USER;
        annualLeaveDTO.fromDate = DEFAULT_FROM_DATE;
        annualLeaveDTO.toDate = DEFAULT_TO_DATE;
        annualLeaveDTO.description = DEFAULT_DESCRIPTION;
        annualLeaveDTO.remarks = DEFAULT_REMARKS;
        annualLeaveDTO.status = DEFAULT_STATUS;
        annualLeaveDTO.organisation = DEFAULT_ORGANISATION;
        return annualLeaveDTO;
    }

    @BeforeEach
    public void initTest() {
        annualLeaveDTO = createEntity();
    }

    @Test
    public void createAnnualLeave() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the AnnualLeave
        annualLeaveDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(annualLeaveDTO)
            .when()
            .post("/api/annual-leaves")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the AnnualLeave in the database
        var annualLeaveDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(annualLeaveDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testAnnualLeaveDTO = annualLeaveDTOList.stream().filter(it -> annualLeaveDTO.id.equals(it.id)).findFirst().get();
        assertThat(testAnnualLeaveDTO.user).isEqualTo(DEFAULT_USER);
        assertThat(testAnnualLeaveDTO.fromDate).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testAnnualLeaveDTO.toDate).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testAnnualLeaveDTO.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAnnualLeaveDTO.remarks).isEqualTo(DEFAULT_REMARKS);
        assertThat(testAnnualLeaveDTO.status).isEqualTo(DEFAULT_STATUS);
        assertThat(testAnnualLeaveDTO.organisation).isEqualTo(DEFAULT_ORGANISATION);
    }

    @Test
    public void createAnnualLeaveWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the AnnualLeave with an existing ID
        annualLeaveDTO.id = new ObjectId("1");

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(annualLeaveDTO)
            .when()
            .post("/api/annual-leaves")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the AnnualLeave in the database
        var annualLeaveDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(annualLeaveDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkUserIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        annualLeaveDTO.user = null;

        // Create the AnnualLeave, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(annualLeaveDTO)
            .when()
            .post("/api/annual-leaves")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the AnnualLeave in the database
        var annualLeaveDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(annualLeaveDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkFromDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        annualLeaveDTO.fromDate = null;

        // Create the AnnualLeave, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(annualLeaveDTO)
            .when()
            .post("/api/annual-leaves")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the AnnualLeave in the database
        var annualLeaveDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(annualLeaveDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkToDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        annualLeaveDTO.toDate = null;

        // Create the AnnualLeave, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(annualLeaveDTO)
            .when()
            .post("/api/annual-leaves")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the AnnualLeave in the database
        var annualLeaveDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(annualLeaveDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkDescriptionIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        annualLeaveDTO.description = null;

        // Create the AnnualLeave, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(annualLeaveDTO)
            .when()
            .post("/api/annual-leaves")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the AnnualLeave in the database
        var annualLeaveDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(annualLeaveDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkStatusIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        annualLeaveDTO.status = null;

        // Create the AnnualLeave, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(annualLeaveDTO)
            .when()
            .post("/api/annual-leaves")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the AnnualLeave in the database
        var annualLeaveDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(annualLeaveDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkOrganisationIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        annualLeaveDTO.organisation = null;

        // Create the AnnualLeave, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(annualLeaveDTO)
            .when()
            .post("/api/annual-leaves")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the AnnualLeave in the database
        var annualLeaveDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(annualLeaveDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateAnnualLeave() {
        // Initialize the database
        annualLeaveDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(annualLeaveDTO)
            .when()
            .post("/api/annual-leaves")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the annualLeave
        var updatedAnnualLeaveDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves/{id}", annualLeaveDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the annualLeave
        updatedAnnualLeaveDTO.user = UPDATED_USER;
        updatedAnnualLeaveDTO.fromDate = UPDATED_FROM_DATE;
        updatedAnnualLeaveDTO.toDate = UPDATED_TO_DATE;
        updatedAnnualLeaveDTO.description = UPDATED_DESCRIPTION;
        updatedAnnualLeaveDTO.remarks = UPDATED_REMARKS;
        updatedAnnualLeaveDTO.status = UPDATED_STATUS;
        updatedAnnualLeaveDTO.organisation = UPDATED_ORGANISATION;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedAnnualLeaveDTO)
            .when()
            .put("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the AnnualLeave in the database
        var annualLeaveDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(annualLeaveDTOList).hasSize(databaseSizeBeforeUpdate);
        var testAnnualLeaveDTO = annualLeaveDTOList.stream().filter(it -> updatedAnnualLeaveDTO.id.equals(it.id)).findFirst().get();
        assertThat(testAnnualLeaveDTO.user).isEqualTo(UPDATED_USER);
        assertThat(testAnnualLeaveDTO.fromDate).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testAnnualLeaveDTO.toDate).isEqualTo(UPDATED_TO_DATE);
        assertThat(testAnnualLeaveDTO.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAnnualLeaveDTO.remarks).isEqualTo(UPDATED_REMARKS);
        assertThat(testAnnualLeaveDTO.status).isEqualTo(UPDATED_STATUS);
        assertThat(testAnnualLeaveDTO.organisation).isEqualTo(UPDATED_ORGANISATION);
    }

    @Test
    public void updateNonExistingAnnualLeave() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
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
            .body(annualLeaveDTO)
            .when()
            .put("/api/annual-leaves")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the AnnualLeave in the database
        var annualLeaveDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(annualLeaveDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteAnnualLeave() {
        // Initialize the database
        annualLeaveDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(annualLeaveDTO)
            .when()
            .post("/api/annual-leaves")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the annualLeave
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/annual-leaves/{id}", annualLeaveDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var annualLeaveDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(annualLeaveDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllAnnualLeaves() {
        // Initialize the database
        annualLeaveDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(annualLeaveDTO)
            .when()
            .post("/api/annual-leaves")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the annualLeaveList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(annualLeaveDTO.id))
            .body("user", hasItem(DEFAULT_USER))            .body("fromDate", hasItem(TestUtil.formatDateTime(DEFAULT_FROM_DATE)))            .body("toDate", hasItem(TestUtil.formatDateTime(DEFAULT_TO_DATE)))            .body("description", hasItem(DEFAULT_DESCRIPTION))            .body("remarks", hasItem(DEFAULT_REMARKS))            .body("status", hasItem(DEFAULT_STATUS.toString()))            .body("organisation", hasItem(DEFAULT_ORGANISATION));
    }

    @Test
    public void getAnnualLeave() {
        // Initialize the database
        annualLeaveDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(annualLeaveDTO)
            .when()
            .post("/api/annual-leaves")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the annualLeave
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/annual-leaves/{id}", annualLeaveDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the annualLeave
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves/{id}", annualLeaveDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(annualLeaveDTO.id))

                .body("user", is(DEFAULT_USER))
                .body("fromDate", is(TestUtil.formatDateTime(DEFAULT_FROM_DATE)))
                .body("toDate", is(TestUtil.formatDateTime(DEFAULT_TO_DATE)))
                .body("description", is(DEFAULT_DESCRIPTION))
                .body("remarks", is(DEFAULT_REMARKS))
                .body("status", is(DEFAULT_STATUS.toString()))
                .body("organisation", is(DEFAULT_ORGANISATION));
    }

    @Test
    public void getNonExistingAnnualLeave() {
        // Get the annualLeave
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/annual-leaves/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
