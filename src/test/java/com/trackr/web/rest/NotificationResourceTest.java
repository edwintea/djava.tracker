package com.trackr.web.rest;

import static com.trackr.domain.enumeration.NotificationType.ANNUAL_LEAVE_REQUEST;
import static com.trackr.domain.enumeration.NotificationType.NEWSLETTER;
import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.trackr.TestUtil;
import com.trackr.domain.enumeration.NotificationType;
import com.trackr.service.dto.NotificationDTO;
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
public class NotificationResourceTest {

    private static final TypeRef<NotificationDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<NotificationDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final NotificationType DEFAULT_TYPE = NEWSLETTER;
    private static final NotificationType UPDATED_TYPE = ANNUAL_LEAVE_REQUEST;

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final String DEFAULT_ORGANISATION = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATION = "BBBBBBBBBB";



    String adminToken;

    NotificationDTO notificationDTO;


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
    public static NotificationDTO createEntity() {
        var notificationDTO = new NotificationDTO();
        notificationDTO.type = DEFAULT_TYPE;
        notificationDTO.subject = DEFAULT_SUBJECT;
        notificationDTO.body = DEFAULT_BODY;
        notificationDTO.createdDate = DEFAULT_CREATED_DATE;
        notificationDTO.user = DEFAULT_USER;
        notificationDTO.organisation = DEFAULT_ORGANISATION;
        return notificationDTO;
    }

    @BeforeEach
    public void initTest() {
        notificationDTO = createEntity();
    }

    @Test
    public void createNotification() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Notification
        notificationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(notificationDTO)
            .when()
            .post("/api/notifications")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the Notification in the database
        var notificationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(notificationDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testNotificationDTO = notificationDTOList.stream().filter(it -> notificationDTO.id.equals(it.id)).findFirst().get();
        assertThat(testNotificationDTO.type).isEqualTo(DEFAULT_TYPE);
        assertThat(testNotificationDTO.subject).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testNotificationDTO.body).isEqualTo(DEFAULT_BODY);
        assertThat(testNotificationDTO.createdDate).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testNotificationDTO.user).isEqualTo(DEFAULT_USER);
        assertThat(testNotificationDTO.organisation).isEqualTo(DEFAULT_ORGANISATION);
    }

    @Test
    public void createNotificationWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Notification with an existing ID
        notificationDTO.id = new ObjectId("1");

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(notificationDTO)
            .when()
            .post("/api/notifications")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Notification in the database
        var notificationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(notificationDTOList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkTypeIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        notificationDTO.type = null;

        // Create the Notification, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(notificationDTO)
            .when()
            .post("/api/notifications")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Notification in the database
        var notificationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(notificationDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkSubjectIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        notificationDTO.subject = null;

        // Create the Notification, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(notificationDTO)
            .when()
            .post("/api/notifications")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Notification in the database
        var notificationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(notificationDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkBodyIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        notificationDTO.body = null;

        // Create the Notification, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(notificationDTO)
            .when()
            .post("/api/notifications")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Notification in the database
        var notificationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(notificationDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkCreatedDateIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        notificationDTO.createdDate = null;

        // Create the Notification, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(notificationDTO)
            .when()
            .post("/api/notifications")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Notification in the database
        var notificationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(notificationDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkUserIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        notificationDTO.user = null;

        // Create the Notification, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(notificationDTO)
            .when()
            .post("/api/notifications")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Notification in the database
        var notificationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(notificationDTOList).hasSize(databaseSizeBeforeTest);
    }
    @Test
    public void checkOrganisationIsRequired() throws Exception {
        var databaseSizeBeforeTest = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // set the field null
        notificationDTO.organisation = null;

        // Create the Notification, which fails.
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(notificationDTO)
            .when()
            .post("/api/notifications")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Notification in the database
        var notificationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(notificationDTOList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void updateNotification() {
        // Initialize the database
        notificationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(notificationDTO)
            .when()
            .post("/api/notifications")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the notification
        var updatedNotificationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications/{id}", notificationDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the notification
        updatedNotificationDTO.type = UPDATED_TYPE;
        updatedNotificationDTO.subject = UPDATED_SUBJECT;
        updatedNotificationDTO.body = UPDATED_BODY;
        updatedNotificationDTO.createdDate = UPDATED_CREATED_DATE;
        updatedNotificationDTO.user = UPDATED_USER;
        updatedNotificationDTO.organisation = UPDATED_ORGANISATION;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedNotificationDTO)
            .when()
            .put("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Notification in the database
        var notificationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(notificationDTOList).hasSize(databaseSizeBeforeUpdate);
        var testNotificationDTO = notificationDTOList.stream().filter(it -> updatedNotificationDTO.id.equals(it.id)).findFirst().get();
        assertThat(testNotificationDTO.type).isEqualTo(UPDATED_TYPE);
        assertThat(testNotificationDTO.subject).isEqualTo(UPDATED_SUBJECT);
        assertThat(testNotificationDTO.body).isEqualTo(UPDATED_BODY);
        assertThat(testNotificationDTO.createdDate).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testNotificationDTO.user).isEqualTo(UPDATED_USER);
        assertThat(testNotificationDTO.organisation).isEqualTo(UPDATED_ORGANISATION);
    }

    @Test
    public void updateNonExistingNotification() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
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
            .body(notificationDTO)
            .when()
            .put("/api/notifications")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Notification in the database
        var notificationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(notificationDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteNotification() {
        // Initialize the database
        notificationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(notificationDTO)
            .when()
            .post("/api/notifications")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the notification
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/notifications/{id}", notificationDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var notificationDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(notificationDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllNotifications() {
        // Initialize the database
        notificationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(notificationDTO)
            .when()
            .post("/api/notifications")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the notificationList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(notificationDTO.id))
            .body("type", hasItem(DEFAULT_TYPE.toString()))            .body("subject", hasItem(DEFAULT_SUBJECT))            .body("body", hasItem(DEFAULT_BODY))            .body("createdDate", hasItem(TestUtil.formatDateTime(DEFAULT_CREATED_DATE)))            .body("user", hasItem(DEFAULT_USER))            .body("organisation", hasItem(DEFAULT_ORGANISATION));
    }

    @Test
    public void getNotification() {
        // Initialize the database
        notificationDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(notificationDTO)
            .when()
            .post("/api/notifications")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the notification
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/notifications/{id}", notificationDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the notification
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications/{id}", notificationDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(notificationDTO.id))

                .body("type", is(DEFAULT_TYPE.toString()))
                .body("subject", is(DEFAULT_SUBJECT))
                .body("body", is(DEFAULT_BODY))
                .body("createdDate", is(TestUtil.formatDateTime(DEFAULT_CREATED_DATE)))
                .body("user", is(DEFAULT_USER))
                .body("organisation", is(DEFAULT_ORGANISATION));
    }

    @Test
    public void getNonExistingNotification() {
        // Get the notification
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/notifications/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
