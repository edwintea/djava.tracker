package com.trackr.web.rest;

import com.trackr.security.jwt.TokenProvider;
import com.trackr.service.AuthenticationService;
import com.trackr.service.EmployeeService;
import com.trackr.service.UserService;
import com.trackr.service.dto.AuthenticationDTO;
import com.trackr.service.dto.EmployeeDTO;
import com.trackr.service.dto.UserDTO;
import com.trackr.web.rest.vm.LoginVM;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.bind.annotation.JsonbProperty;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller to authenticate users.
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserJWTController {
    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    final AuthenticationService authenticationService;

    final TokenProvider tokenProvider;

    final UserService userService;

    final EmployeeService employeeService;

    @Inject
    public UserJWTController(AuthenticationService authenticationService, TokenProvider tokenProvider, UserService userService, EmployeeService employeeService) {
        this.authenticationService = authenticationService;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.employeeService = employeeService;
    }

    @POST
    @Path("/authenticate")
    @PermitAll
    public Response authorize(@Valid LoginVM loginVM) {
        try {
            QuarkusSecurityIdentity identity = authenticationService.authenticate(loginVM.username, loginVM.password);
            boolean rememberMe = (loginVM.rememberMe == null) ? false : loginVM.rememberMe;
            String jwt = tokenProvider.createToken(identity, rememberMe);
            UserDTO user = userService
                .getUserWithAuthoritiesByLogin(identity.getPrincipal().getName())
                .map(UserDTO::new).orElseThrow();
            EmployeeDTO employee = employeeService.findOneByUserId(user.id.toString()).orElseThrow();
            AuthenticationDTO authentication = new AuthenticationDTO(new JWTToken(jwt).idToken, user, employee);
            return Response.ok().entity(authentication).header("Authorization", "Bearer " + jwt).build();
        } catch (SecurityException e) {
            throw new UnauthorizedException();
        }
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    @RegisterForReflection
    public static class JWTToken {
        @JsonbProperty("id_token")
        public String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
