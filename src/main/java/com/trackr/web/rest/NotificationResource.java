package com.trackr.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.trackr.service.NotificationService;
import com.trackr.repository.NotificationRepository;
import com.trackr.web.rest.errors.BadRequestAlertException;
import com.trackr.web.util.HeaderUtil;
import com.trackr.web.util.ResponseUtil;
import com.trackr.service.dto.NotificationDTO;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.trackr.service.Paged;
import com.trackr.web.rest.vm.PageRequestVM;
import com.trackr.web.rest.vm.SortRequestVM;
import com.trackr.web.util.PaginationUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.trackr.domain.Notification}.
 */
@Path("/api/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    private static final String ENTITY_NAME = "notification";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    NotificationService notificationService;
    /**
     * {@code POST  /notifications} : Create a new notification.
     *
     * @param notificationDTO the notificationDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new notificationDTO, or with status {@code 400 (Bad Request)} if the notification has already an ID.
     */
    @POST
    public Response createNotification(@Valid NotificationDTO notificationDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Notification : {}", notificationDTO);
        if (notificationDTO.id != null) {
            throw new BadRequestAlertException("A new notification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = notificationService.persistOrUpdate(notificationDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /notifications} : Updates an existing notification.
     *
     * @param notificationDTO the notificationDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated notificationDTO,
     * or with status {@code 400 (Bad Request)} if the notificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationDTO couldn't be updated.
     */
    @PUT
    public Response updateNotification(@Valid NotificationDTO notificationDTO) {
        log.debug("REST request to update Notification : {}", notificationDTO);
        if (notificationDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = notificationService.persistOrUpdate(notificationDTO);
        var response = Response.ok().entity(notificationDTO);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, notificationDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /notifications/:id} : delete the "id" notification.
     *
     * @param id the id of the notificationDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteNotification(@PathParam("id") String id) {
        log.debug("REST request to delete Notification : {}", id);
        notificationService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /notifications} : get all the notifications.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of notifications in body.
     */
    @GET
    public Response getAllNotifications(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Notifications");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<NotificationDTO> result = notificationService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }


    /**
     * {@code GET  /notifications/:id} : get the "id" notification.
     *
     * @param id the id of the notificationDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the notificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getNotification(@PathParam("id") String id) {
        log.debug("REST request to get Notification : {}", id);
        Optional<NotificationDTO> notificationDTO = notificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationDTO);
    }
}
