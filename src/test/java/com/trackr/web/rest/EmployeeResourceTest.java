package com.trackr.web.rest;

import static com.trackr.domain.enumeration.EmployeeType.INTERN;
import static com.trackr.domain.enumeration.EmployeeType.PERMANENT;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.trackr.TestUtil;
import com.trackr.domain.enumeration.EmployeeType;
import com.trackr.service.dto.EmployeeDTO;
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
public class EmployeeResourceTest {

    private static final TypeRef<EmployeeDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<EmployeeDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final Boolean DEFAULT_IS_ASSIGNED = false;
    private static final Boolean UPDATED_IS_ASSIGNED = true;

    private static final EmployeeType DEFAULT_TYPE = PERMANENT;
    private static final EmployeeType UPDATED_TYPE = INTERN;

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_ASSIGNMENT = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNMENT = "BBBBBBBBBB";

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String DEFAULT_REPORTS_TO = "AAAAAAAAAA";
    private static final String UPDATED_REPORTS_TO = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANISATION = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_SINCE_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_SINCE_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final Boolean DEFAULT_IS_ASSIGNABLE = false;
    private static final Boolean UPDATED_IS_ASSIGNABLE = true;



    String adminToken;

    EmployeeDTO employeeDTO;


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
    public static EmployeeDTO createEntity() {
        var employeeDTO = new EmployeeDTO();
        employeeDTO.isAssigned = DEFAULT_IS_ASSIGNED;
        employeeDTO.type = DEFAULT_TYPE;
        employeeDTO.position = DEFAULT_POSITION;
        employeeDTO.user = DEFAULT_USER;
        employeeDTO.reportsTo = DEFAULT_REPORTS_TO;
        employeeDTO.organisation = DEFAULT_ORGANISATION;
        employeeDTO.sinceDate = DEFAULT_SINCE_DATE;
        employeeDTO.isAssignable = DEFAULT_IS_ASSIGNABLE;
        return employeeDTO;
    }

    @BeforeEach
    public void initTest() {
        employeeDTO = createEntity();
    }

    @Test
    public void createEmployee() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Employee
        employeeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the Employee in the database
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testEmployeeDTO = employeeDTOList.stream().filter(it -> employeeDTO.id.equals(it.id)).findFirst().get();
        assertThat(testEmployeeDTO.isAssigned).isEqualTo(DEFAULT_IS_ASSIGNED);
        assertThat(testEmployeeDTO.type).isEqualTo(DEFAULT_TYPE);
        assertThat(testEmployeeDTO.position).isEqualTo(DEFAULT_POSITION);
        assertThat(testEmployeeDTO.user).isEqualTo(DEFAULT_USER);
        assertThat(testEmployeeDTO.reportsTo).isEqualTo(DEFAULT_REPORTS_TO);
        assertThat(testEmployeeDTO.organisation).isEqualTo(DEFAULT_ORGANISATION);
        assertThat(testEmployeeDTO.sinceDate).isEqualTo(DEFAULT_SINCE_DATE);
        assertThat(testEmployeeDTO.isAssignable).isEqualTo(DEFAULT_IS_ASSIGNABLE);
    }

    @Test
    public void createEmployeeWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Employee with an existing ID
        employeeDTO.id = new ObjectId("1");

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Employee in the database
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkIsAssignedIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        employeeDTO.isAssigned = null;

        // Create the Employee, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Employee in the database
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkTypeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        employeeDTO.type = null;

        // Create the Employee, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Employee in the database
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkUserIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        employeeDTO.user = null;

        // Create the Employee, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Employee in the database
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkOrganisationIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        employeeDTO.organisation = null;

        // Create the Employee, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Employee in the database
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkIsAssignableIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        employeeDTO.isAssignable = null;

        // Create the Employee, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Employee in the database
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateEmployee() {
        // Initialize the database
        employeeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the employee
        var updatedEmployeeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees/{id}", employeeDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the employee
        updatedEmployeeDTO.isAssigned = UPDATED_IS_ASSIGNED;
        updatedEmployeeDTO.type = UPDATED_TYPE;
        updatedEmployeeDTO.position = UPDATED_POSITION;
        updatedEmployeeDTO.user = UPDATED_USER;
        updatedEmployeeDTO.reportsTo = UPDATED_REPORTS_TO;
        updatedEmployeeDTO.organisation = UPDATED_ORGANISATION;
        updatedEmployeeDTO.sinceDate = UPDATED_SINCE_DATE;
        updatedEmployeeDTO.isAssignable = UPDATED_IS_ASSIGNABLE;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedEmployeeDTO)
            .when()
            .put("/api/employees")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Employee in the database
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeUpdate);
        var testEmployeeDTO = employeeDTOList.stream().filter(it -> updatedEmployeeDTO.id.equals(it.id)).findFirst().get();
        assertThat(testEmployeeDTO.isAssigned).isEqualTo(UPDATED_IS_ASSIGNED);
        assertThat(testEmployeeDTO.type).isEqualTo(UPDATED_TYPE);
        assertThat(testEmployeeDTO.position).isEqualTo(UPDATED_POSITION);
        assertThat(testEmployeeDTO.user).isEqualTo(UPDATED_USER);
        assertThat(testEmployeeDTO.reportsTo).isEqualTo(UPDATED_REPORTS_TO);
        assertThat(testEmployeeDTO.organisation).isEqualTo(UPDATED_ORGANISATION);
        assertThat(testEmployeeDTO.sinceDate).isEqualTo(UPDATED_SINCE_DATE);
        assertThat(testEmployeeDTO.isAssignable).isEqualTo(UPDATED_IS_ASSIGNABLE);
    }

    @Test
    public void updateNonExistingEmployee() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
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
            .body(employeeDTO)
            .when()
            .put("/api/employees")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Employee in the database
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteEmployee() {
        // Initialize the database
        employeeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the employee
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/employees/{id}", employeeDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllEmployees() {
        // Initialize the database
        employeeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the employeeList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(employeeDTO.id))
            .body("isAssigned", hasItem(DEFAULT_IS_ASSIGNED.booleanValue()))            .body("type", hasItem(DEFAULT_TYPE.toString()))            .body("position", hasItem(DEFAULT_POSITION))            .body("assignment", hasItem(DEFAULT_ASSIGNMENT))            .body("user", hasItem(DEFAULT_USER))            .body("reportsTo", hasItem(DEFAULT_REPORTS_TO))            .body("organisation", hasItem(DEFAULT_ORGANISATION))            .body("sinceDate", hasItem(TestUtil.formatDateTime(DEFAULT_SINCE_DATE)))            .body("isAssignable", hasItem(DEFAULT_IS_ASSIGNABLE.booleanValue()));
    }

    @Test
    public void getEmployee() {
        // Initialize the database
        employeeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the employee
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/employees/{id}", employeeDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the employee
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees/{id}", employeeDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(employeeDTO.id))

                .body("isAssigned", is(DEFAULT_IS_ASSIGNED.booleanValue()))
                .body("type", is(DEFAULT_TYPE.toString()))
                .body("position", is(DEFAULT_POSITION))
                .body("assignment", is(DEFAULT_ASSIGNMENT))
                .body("user", is(DEFAULT_USER))
                .body("reportsTo", is(DEFAULT_REPORTS_TO))
                .body("organisation", is(DEFAULT_ORGANISATION))
                .body("sinceDate", is(TestUtil.formatDateTime(DEFAULT_SINCE_DATE)))
                .body("isAssignable", is(DEFAULT_IS_ASSIGNABLE.booleanValue()));
    }

    @Test
    public void getNonExistingEmployee() {
        // Get the employee
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
