package com.trackr.web.rest;

import static com.trackr.domain.enumeration.ClientType.MIXED;
import static com.trackr.domain.enumeration.ClientType.OUTSOURCE;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.trackr.TestUtil;
import com.trackr.domain.enumeration.ClientType;
import com.trackr.service.dto.ClientDTO;
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
public class ClientResourceTest {

    private static final TypeRef<ClientDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<ClientDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_SINCE_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_SINCE_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final ClientType DEFAULT_TYPE = MIXED;
    private static final ClientType UPDATED_TYPE = OUTSOURCE;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANISATION = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION = "BBBBBBBBBB";



    String adminToken;

    ClientDTO clientDTO;


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
    public static ClientDTO createEntity() {
        var clientDTO = new ClientDTO();
        clientDTO.name = DEFAULT_NAME;
        clientDTO.sinceDate = DEFAULT_SINCE_DATE;
        clientDTO.address = DEFAULT_ADDRESS;
        clientDTO.organisation = DEFAULT_ORGANISATION;
        return clientDTO;
    }

    @BeforeEach
    public void initTest() {
        clientDTO = createEntity();
    }

    @Test
    public void createClient() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Client
        clientDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clientDTO)
            .when()
            .post("/api/clients")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the Client in the database
        var clientDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(clientDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testClientDTO = clientDTOList.stream().filter(it -> clientDTO.id.equals(it.id)).findFirst().get();
        assertThat(testClientDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testClientDTO.sinceDate).isEqualTo(DEFAULT_SINCE_DATE);
        assertThat(testClientDTO.address).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testClientDTO.organisation).isEqualTo(DEFAULT_ORGANISATION);
    }

    @Test
    public void createClientWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Client with an existing ID
        clientDTO.id = new ObjectId("1");

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clientDTO)
            .when()
            .post("/api/clients")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Client in the database
        var clientDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(clientDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        clientDTO.name = null;

        // Create the Client, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clientDTO)
            .when()
            .post("/api/clients")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Client in the database
        var clientDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(clientDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkSinceDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        clientDTO.sinceDate = null;

        // Create the Client, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clientDTO)
            .when()
            .post("/api/clients")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Client in the database
        var clientDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(clientDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkTypeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Client, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clientDTO)
            .when()
            .post("/api/clients")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Client in the database
        var clientDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(clientDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkOrganisationIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        clientDTO.organisation = null;

        // Create the Client, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clientDTO)
            .when()
            .post("/api/clients")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Client in the database
        var clientDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(clientDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateClient() {
        // Initialize the database
        clientDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clientDTO)
            .when()
            .post("/api/clients")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the client
        var updatedClientDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients/{id}", clientDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the client
        updatedClientDTO.name = UPDATED_NAME;
        updatedClientDTO.sinceDate = UPDATED_SINCE_DATE;
        updatedClientDTO.address = UPDATED_ADDRESS;
        updatedClientDTO.organisation = UPDATED_ORGANISATION;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedClientDTO)
            .when()
            .put("/api/clients")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Client in the database
        var clientDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(clientDTOList).hasSize(databaseSizeBeforeUpdate);
        var testClientDTO = clientDTOList.stream().filter(it -> updatedClientDTO.id.equals(it.id)).findFirst().get();
        assertThat(testClientDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testClientDTO.sinceDate).isEqualTo(UPDATED_SINCE_DATE);
        assertThat(testClientDTO.address).isEqualTo(UPDATED_ADDRESS);
        assertThat(testClientDTO.organisation).isEqualTo(UPDATED_ORGANISATION);
    }

    @Test
    public void updateNonExistingClient() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
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
            .body(clientDTO)
            .when()
            .put("/api/clients")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Client in the database
        var clientDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(clientDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteClient() {
        // Initialize the database
        clientDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clientDTO)
            .when()
            .post("/api/clients")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the client
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/clients/{id}", clientDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var clientDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(clientDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllClients() {
        // Initialize the database
        clientDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clientDTO)
            .when()
            .post("/api/clients")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the clientList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(clientDTO.id))
            .body("name", hasItem(DEFAULT_NAME))            .body("sinceDate", hasItem(TestUtil.formatDateTime(DEFAULT_SINCE_DATE)))            .body("type", hasItem(DEFAULT_TYPE.toString()))            .body("address", hasItem(DEFAULT_ADDRESS))            .body("organisation", hasItem(DEFAULT_ORGANISATION));
    }

    @Test
    public void getClient() {
        // Initialize the database
        clientDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(clientDTO)
            .when()
            .post("/api/clients")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the client
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/clients/{id}", clientDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the client
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients/{id}", clientDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(clientDTO.id))

                .body("name", is(DEFAULT_NAME))
                .body("sinceDate", is(TestUtil.formatDateTime(DEFAULT_SINCE_DATE)))
                .body("type", is(DEFAULT_TYPE.toString()))
                .body("address", is(DEFAULT_ADDRESS))
                .body("organisation", is(DEFAULT_ORGANISATION));
    }

    @Test
    public void getNonExistingClient() {
        // Get the client
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/clients/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
