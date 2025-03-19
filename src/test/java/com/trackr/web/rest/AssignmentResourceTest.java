package com.trackr.web.rest;

import static com.trackr.domain.enumeration.AssignmentType.OUTSOURCE;
import static com.trackr.domain.enumeration.AssignmentType.PROJECT;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.trackr.TestUtil;
import com.trackr.domain.enumeration.AssignmentType;
import com.trackr.service.dto.AssignmentDTO;
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
public class AssignmentResourceTest {

    private static final TypeRef<AssignmentDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<AssignmentDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final Instant DEFAULT_TO_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_TO_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final AssignmentType DEFAULT_TYPE = OUTSOURCE;
    private static final AssignmentType UPDATED_TYPE = PROJECT;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANISATION = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION = "BBBBBBBBBB";



    String adminToken;

    AssignmentDTO assignmentDTO;


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
    public static AssignmentDTO createEntity() {
        var assignmentDTO = new AssignmentDTO();
        assignmentDTO.client = DEFAULT_CLIENT;
        assignmentDTO.assignmentType = DEFAULT_TYPE;
        assignmentDTO.description = DEFAULT_DESCRIPTION;
        assignmentDTO.organisation = DEFAULT_ORGANISATION;
        return assignmentDTO;
    }

    @BeforeEach
    public void initTest() {
        assignmentDTO = createEntity();
    }

    @Test
    public void createAssignment() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Assignment
        assignmentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assignmentDTO)
            .when()
            .post("/api/assignments")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the Assignment in the database
        var assignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(assignmentDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testAssignmentDTO = assignmentDTOList.stream().filter(it -> assignmentDTO.id.equals(it.id)).findFirst().get();
        assertThat(testAssignmentDTO.client).isEqualTo(DEFAULT_CLIENT);
        assertThat(testAssignmentDTO.assignmentType).isEqualTo(DEFAULT_TYPE);
        assertThat(testAssignmentDTO.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssignmentDTO.organisation).isEqualTo(DEFAULT_ORGANISATION);
    }

    @Test
    public void createAssignmentWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Assignment with an existing ID
        assignmentDTO.id = new ObjectId("1");

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assignmentDTO)
            .when()
            .post("/api/assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Assignment in the database
        var assignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(assignmentDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkClientIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        assignmentDTO.client = null;

        // Create the Assignment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assignmentDTO)
            .when()
            .post("/api/assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Assignment in the database
        var assignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(assignmentDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkStartDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Assignment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assignmentDTO)
            .when()
            .post("/api/assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Assignment in the database
        var assignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(assignmentDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkTypeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        assignmentDTO.assignmentType = null;

        // Create the Assignment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assignmentDTO)
            .when()
            .post("/api/assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Assignment in the database
        var assignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(assignmentDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkOrganisationIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        assignmentDTO.organisation = null;

        // Create the Assignment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assignmentDTO)
            .when()
            .post("/api/assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Assignment in the database
        var assignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(assignmentDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateAssignment() {
        // Initialize the database
        assignmentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assignmentDTO)
            .when()
            .post("/api/assignments")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the assignment
        var updatedAssignmentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments/{id}", assignmentDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the assignment
        updatedAssignmentDTO.client = UPDATED_CLIENT;
        updatedAssignmentDTO.assignmentType = UPDATED_TYPE;
        updatedAssignmentDTO.description = UPDATED_DESCRIPTION;
        updatedAssignmentDTO.organisation = UPDATED_ORGANISATION;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedAssignmentDTO)
            .when()
            .put("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Assignment in the database
        var assignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(assignmentDTOList).hasSize(databaseSizeBeforeUpdate);
        var testAssignmentDTO = assignmentDTOList.stream().filter(it -> updatedAssignmentDTO.id.equals(it.id)).findFirst().get();
        assertThat(testAssignmentDTO.client).isEqualTo(UPDATED_CLIENT);
        assertThat(testAssignmentDTO.assignmentType).isEqualTo(UPDATED_TYPE);
        assertThat(testAssignmentDTO.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssignmentDTO.organisation).isEqualTo(UPDATED_ORGANISATION);
    }

    @Test
    public void updateNonExistingAssignment() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
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
            .body(assignmentDTO)
            .when()
            .put("/api/assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Assignment in the database
        var assignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(assignmentDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteAssignment() {
        // Initialize the database
        assignmentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assignmentDTO)
            .when()
            .post("/api/assignments")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the assignment
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/assignments/{id}", assignmentDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var assignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(assignmentDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllAssignments() {
        // Initialize the database
        assignmentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assignmentDTO)
            .when()
            .post("/api/assignments")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the assignmentList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(assignmentDTO.id))
            .body("client", hasItem(DEFAULT_CLIENT))            .body("startDate", hasItem(TestUtil.formatDateTime(DEFAULT_START_DATE)))            .body("toDate", hasItem(TestUtil.formatDateTime(DEFAULT_TO_DATE)))            .body("assignmentType", hasItem(DEFAULT_TYPE.toString()))            .body("description", hasItem(DEFAULT_DESCRIPTION))            .body("organisation", hasItem(DEFAULT_ORGANISATION));
    }

    @Test
    public void getAssignment() {
        // Initialize the database
        assignmentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(assignmentDTO)
            .when()
            .post("/api/assignments")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the assignment
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/assignments/{id}", assignmentDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the assignment
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments/{id}", assignmentDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(assignmentDTO.id))

                .body("client", is(DEFAULT_CLIENT))
                .body("startDate", is(TestUtil.formatDateTime(DEFAULT_START_DATE)))
                .body("toDate", is(TestUtil.formatDateTime(DEFAULT_TO_DATE)))
                .body("assignmentType", is(DEFAULT_TYPE.toString()))
                .body("description", is(DEFAULT_DESCRIPTION))
                .body("organisation", is(DEFAULT_ORGANISATION));
    }

    @Test
    public void getNonExistingAssignment() {
        // Get the assignment
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/assignments/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
