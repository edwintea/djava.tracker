package com.trackr.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.trackr.domain.User;
import com.trackr.security.AuthoritiesConstants;
import com.trackr.service.MailService;
import com.trackr.service.UserService;
import com.trackr.service.dto.UserDTO;
import com.trackr.web.rest.errors.BadRequestAlertException;
import com.trackr.web.rest.errors.EmailAlreadyUsedException;
import com.trackr.web.rest.errors.LoginAlreadyUsedException;
import com.trackr.web.util.HeaderUtil;
import com.trackr.web.util.ResponseUtil;
import java.util.List;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserResource {
    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "users";

    final String applicationName;

    final MailService mailService;

    final UserService userService;

    @Inject
    public UserResource(
        @ConfigProperty(name = "application.name") String applicationName,
        MailService mailService,
        UserService userService
    ) {
        this.applicationName = applicationName;
        this.mailService = mailService;
        this.userService = userService;
    }

    /**
     * {@code POST  /users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @POST
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public Response createUser(@Valid UserDTO userDTO) {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.id != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (User.findOneByLogin(userDTO.login.toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (User.findOneByEmailIgnoreCase(userDTO.email).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            var newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            Response.ResponseBuilder response = Response.created(fromPath("/api/users").path(newUser.login).build()).entity(newUser);
            HeaderUtil.createAlert(applicationName, "userManagement.created", newUser.login).forEach(response::header);
            return response.build();
        }
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PUT
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public Response updateUser(@Valid UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = User.findOneByEmailIgnoreCase(userDTO.email);
        if (existingUser.isPresent() && (!existingUser.get().id.equals(userDTO.id))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = User.findOneByLogin(userDTO.login.toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().id.equals(userDTO.id))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);
        return ResponseUtil.wrapOrNotFound(updatedUser, HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.login));
    }

    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{login}")
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public Response deleteUser(@PathParam("login") String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        Response.ResponseBuilder response = Response.noContent();
        HeaderUtil.createAlert(applicationName, "userManagement.deleted", login).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pagination the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and with body all users.
     */
    @GET
    public Response getAllUsers(@QueryParam("sort") String pagination) {
        final List<UserDTO> page = userService.getAllManagedUsers();
        //        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        Response.ResponseBuilder response = Response.ok(page);
        return response.build();
    }

    /**
     * Gets a list of all roles.
     *
     * @return a string list of all roles.
     */
    @GET
    @Path("/authorities")
    @RolesAllowed("ROLE_ADMIN")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GET
    //    @Path("/{login : " + Constants.LOGIN_REGEX + "}")
    @Path("/{login}")
    public Response getUser(@PathParam("login") String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(userService.getUserWithAuthoritiesByLogin(login).map(UserDTO::new));
    }

}
