package com.trackr.web.rest;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

import com.trackr.TestUtil;
import com.trackr.web.rest.vm.LoginVM;
import com.trackr.web.rest.vm.ManagedUserVM;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import java.util.List;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;

import org.junit.jupiter.api.*;

@QuarkusTest
public class UserJWTControllerTest {


    @Inject
    MockMailbox mailbox;


    private void registerUser(ManagedUserVM user) {
        //Registering user
        given()
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(user)
            .when()
            .post("/api/register")
            .then()
            .statusCode(CREATED.getStatusCode());
    }

    private void activateUser(String email) {
        List<Mail> sent = mailbox.getMessagesSentTo(email.toLowerCase());
        Mail creationEmail = sent.get(sent.size() - 1); // get the last mail
        var matcher = Pattern.compile(".*key=(\\w+).*", Pattern.MULTILINE).matcher(creationEmail.getHtml());

        if (!matcher.find()) {
            fail("No key found in activation mail");
        }

        var key = matcher.group(1);

        //Activating user
        given().contentType(APPLICATION_JSON).accept(APPLICATION_JSON).when().get("/api/activate?key={key}", key);
    }

    @Test
    public void testAuthorize() {
        var user = new ManagedUserVM();
        user.login = "user-jwt-controller";
        user.email = "user-jwt-controller@example.com";
        user.password = "test";

        registerUser(user);
        activateUser(user.email);
        var token = TestUtil.getToken(user.login, user.password);

        var login = new LoginVM();
        login.username = "user-jwt-controller";
        login.password = "test";

        given()
            .body(login)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .when()
            .post("/api/authenticate")
            .then()
            .statusCode(OK.getStatusCode())
            .body("id_token", instanceOf(String.class))
            .body("id_token", notNullValue())
            .header(HttpHeaders.AUTHORIZATION, not(blankOrNullString()));
    }

    @Test
    public void testAuthorizeWithRememberMe() {
        var user = new ManagedUserVM();
        user.login = "user-jwt-controller-remember-me";
        user.email = "user-jwt-controller-remember-me@example.com";
        user.activated = true;
        user.password = "test";

        registerUser(user);
        activateUser(user.email);
        var token = TestUtil.getToken(user.login, user.password);

        var login = new LoginVM();
        login.username = "user-jwt-controller-remember-me";
        login.password = "test";
        login.rememberMe = true;

        given()
            .body(login)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .when()
            .post("/api/authenticate")
            .then()
            .statusCode(OK.getStatusCode())
            .body("id_token", instanceOf(String.class))
            .body("id_token", notNullValue())
            .header(HttpHeaders.AUTHORIZATION, not(blankOrNullString()));
    }

    @Test
    public void testAuthorizeFails() {
        var login = new LoginVM();
        login.username = "wrong-user";
        login.password = "wrong password";

        given()
            .body(login)
            .contentType(APPLICATION_JSON)
            .when()
            .post("/api/authenticate")
            .then()
            .statusCode(UNAUTHORIZED.getStatusCode())
            .header(HttpHeaders.AUTHORIZATION, nullValue());
    }
}
