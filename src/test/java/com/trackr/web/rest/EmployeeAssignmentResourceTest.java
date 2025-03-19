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
import com.trackr.service.dto.EmployeeAssignmentDTO;
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
public class EmployeeAssignmentResourceTest {

    private static final TypeRef<EmployeeAssignmentDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<EmployeeAssignmentDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_ASSIGNMENT = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNMENT = "BBBBBBBBBB";

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

    private static final String DEFAULT_CLIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANISATION = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION = "BBBBBBBBBB";



    String adminToken;

    EmployeeAssignmentDTO employeeAssignmentDTO;


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
    public static EmployeeAssignmentDTO createEntity() {
        var employeeAssignmentDTO = new EmployeeAssignmentDTO();
        employeeAssignmentDTO.active = DEFAULT_ACTIVE;
        employeeAssignmentDTO.client = DEFAULT_CLIENT;
        employeeAssignmentDTO.startDate = DEFAULT_START_DATE;
        employeeAssignmentDTO.toDate = DEFAULT_TO_DATE;
        employeeAssignmentDTO.assignmentType = DEFAULT_TYPE;
        employeeAssignmentDTO.description = DEFAULT_DESCRIPTION;
        employeeAssignmentDTO.clientName = DEFAULT_CLIENT_NAME;
        employeeAssignmentDTO.user = DEFAULT_USER;
        employeeAssignmentDTO.organisation = DEFAULT_ORGANISATION;
        return employeeAssignmentDTO;
    }

    @BeforeEach
    public void initTest() {
        employeeAssignmentDTO = createEntity();
    }

    @Test
    public void createEmployeeAssignment() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the EmployeeAssignment
        employeeAssignmentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the EmployeeAssignment in the database
        var employeeAssignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeAssignmentDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testEmployeeAssignmentDTO = employeeAssignmentDTOList.stream().filter(it -> employeeAssignmentDTO.id.equals(it.id)).findFirst().get();
        assertThat(testEmployeeAssignmentDTO.active).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testEmployeeAssignmentDTO.client).isEqualTo(DEFAULT_CLIENT);
        assertThat(testEmployeeAssignmentDTO.startDate).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEmployeeAssignmentDTO.toDate).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testEmployeeAssignmentDTO.assignmentType).isEqualTo(DEFAULT_TYPE);
        assertThat(testEmployeeAssignmentDTO.description).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEmployeeAssignmentDTO.clientName).isEqualTo(DEFAULT_CLIENT_NAME);
        assertThat(testEmployeeAssignmentDTO.user).isEqualTo(DEFAULT_USER);
        assertThat(testEmployeeAssignmentDTO.organisation).isEqualTo(DEFAULT_ORGANISATION);
    }

    @Test
    public void createEmployeeAssignmentWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the EmployeeAssignment with an existing ID
        employeeAssignmentDTO.id = new ObjectId( "1");

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the EmployeeAssignment in the database
        var employeeAssignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeAssignmentDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkActiveIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        employeeAssignmentDTO.active = null;

        // Create the EmployeeAssignment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the EmployeeAssignment in the database
        var employeeAssignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeAssignmentDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkAssignmentIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the EmployeeAssignment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the EmployeeAssignment in the database
        var employeeAssignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeAssignmentDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkClientIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        employeeAssignmentDTO.client = null;

        // Create the EmployeeAssignment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the EmployeeAssignment in the database
        var employeeAssignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeAssignmentDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkStartDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        employeeAssignmentDTO.startDate = null;

        // Create the EmployeeAssignment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the EmployeeAssignment in the database
        var employeeAssignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeAssignmentDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkTypeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        employeeAssignmentDTO.assignmentType = null;

        // Create the EmployeeAssignment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the EmployeeAssignment in the database
        var employeeAssignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeAssignmentDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkClientNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        employeeAssignmentDTO.clientName = null;

        // Create the EmployeeAssignment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the EmployeeAssignment in the database
        var employeeAssignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeAssignmentDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkUserIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        employeeAssignmentDTO.user = null;

        // Create the EmployeeAssignment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the EmployeeAssignment in the database
        var employeeAssignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeAssignmentDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkOrganisationIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        employeeAssignmentDTO.organisation = null;

        // Create the EmployeeAssignment, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the EmployeeAssignment in the database
        var employeeAssignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeAssignmentDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateEmployeeAssignment() {
        // Initialize the database
        employeeAssignmentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the employeeAssignment
        var updatedEmployeeAssignmentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments/{id}", employeeAssignmentDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the employeeAssignment
        updatedEmployeeAssignmentDTO.active = UPDATED_ACTIVE;
        updatedEmployeeAssignmentDTO.client = UPDATED_CLIENT;
        updatedEmployeeAssignmentDTO.startDate = UPDATED_START_DATE;
        updatedEmployeeAssignmentDTO.toDate = UPDATED_TO_DATE;
        updatedEmployeeAssignmentDTO.assignmentType = UPDATED_TYPE;
        updatedEmployeeAssignmentDTO.description = UPDATED_DESCRIPTION;
        updatedEmployeeAssignmentDTO.clientName = UPDATED_CLIENT_NAME;
        updatedEmployeeAssignmentDTO.user = UPDATED_USER;
        updatedEmployeeAssignmentDTO.organisation = UPDATED_ORGANISATION;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedEmployeeAssignmentDTO)
            .when()
            .put("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the EmployeeAssignment in the database
        var employeeAssignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeAssignmentDTOList).hasSize(databaseSizeBeforeUpdate);
        var testEmployeeAssignmentDTO = employeeAssignmentDTOList.stream().filter(it -> updatedEmployeeAssignmentDTO.id.equals(it.id)).findFirst().get();
        assertThat(testEmployeeAssignmentDTO.active).isEqualTo(UPDATED_ACTIVE);
        assertThat(testEmployeeAssignmentDTO.client).isEqualTo(UPDATED_CLIENT);
        assertThat(testEmployeeAssignmentDTO.startDate).isEqualTo(UPDATED_START_DATE);
        assertThat(testEmployeeAssignmentDTO.toDate).isEqualTo(UPDATED_TO_DATE);
        assertThat(testEmployeeAssignmentDTO.assignmentType).isEqualTo(UPDATED_TYPE);
        assertThat(testEmployeeAssignmentDTO.description).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmployeeAssignmentDTO.clientName).isEqualTo(UPDATED_CLIENT_NAME);
        assertThat(testEmployeeAssignmentDTO.user).isEqualTo(UPDATED_USER);
        assertThat(testEmployeeAssignmentDTO.organisation).isEqualTo(UPDATED_ORGANISATION);
    }

    @Test
    public void updateNonExistingEmployeeAssignment() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
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
            .body(employeeAssignmentDTO)
            .when()
            .put("/api/employee-assignments")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the EmployeeAssignment in the database
        var employeeAssignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeAssignmentDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteEmployeeAssignment() {
        // Initialize the database
        employeeAssignmentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the employeeAssignment
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/employee-assignments/{id}", employeeAssignmentDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var employeeAssignmentDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeAssignmentDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllEmployeeAssignments() {
        // Initialize the database
        employeeAssignmentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the employeeAssignmentList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(employeeAssignmentDTO.id))
            .body("active", hasItem(DEFAULT_ACTIVE.booleanValue()))            .body("assignment", hasItem(DEFAULT_ASSIGNMENT))            .body("client", hasItem(DEFAULT_CLIENT))            .body("startDate", hasItem(TestUtil.formatDateTime(DEFAULT_START_DATE)))            .body("toDate", hasItem(TestUtil.formatDateTime(DEFAULT_TO_DATE)))            .body("assignmentType", hasItem(DEFAULT_TYPE.toString()))            .body("description", hasItem(DEFAULT_DESCRIPTION))            .body("clientName", hasItem(DEFAULT_CLIENT_NAME))            .body("user", hasItem(DEFAULT_USER))            .body("organisation", hasItem(DEFAULT_ORGANISATION));
    }

    @Test
    public void getEmployeeAssignment() {
        // Initialize the database
        employeeAssignmentDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeAssignmentDTO)
            .when()
            .post("/api/employee-assignments")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the employeeAssignment
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/employee-assignments/{id}", employeeAssignmentDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the employeeAssignment
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments/{id}", employeeAssignmentDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(employeeAssignmentDTO.id))

                .body("active", is(DEFAULT_ACTIVE.booleanValue()))
                .body("assignment", is(DEFAULT_ASSIGNMENT))
                .body("client", is(DEFAULT_CLIENT))
                .body("startDate", is(TestUtil.formatDateTime(DEFAULT_START_DATE)))
                .body("toDate", is(TestUtil.formatDateTime(DEFAULT_TO_DATE)))
                .body("assignmentType", is(DEFAULT_TYPE.toString()))
                .body("description", is(DEFAULT_DESCRIPTION))
                .body("clientName", is(DEFAULT_CLIENT_NAME))
                .body("user", is(DEFAULT_USER))
                .body("organisation", is(DEFAULT_ORGANISATION));
    }

    @Test
    public void getNonExistingEmployeeAssignment() {
        // Get the employeeAssignment
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employee-assignments/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
