package com.trackr.web.rest;

import com.trackr.security.AuthoritiesConstants;
import com.trackr.service.AssignmentService;
import com.trackr.service.Paged;
import com.trackr.service.dto.AssignmentDTO;
import com.trackr.service.dto.AssignmentEmployeeCountDTO;
import com.trackr.web.rest.errors.BadRequestAlertException;
import com.trackr.web.rest.vm.PageRequestVM;
import com.trackr.web.rest.vm.SortRequestVM;
import com.trackr.web.util.HeaderUtil;
import com.trackr.web.util.PaginationUtil;
import com.trackr.web.util.RequestHeaderUtil;
import com.trackr.web.util.ResponseUtil;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.UriBuilder.fromPath;

/**
 * REST controller for managing {@link com.trackr.domain.Assignment}.
 */
@Path("/api/assignments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AssignmentResource {

    private final Logger log = LoggerFactory.getLogger(AssignmentResource.class);

    private static final String ENTITY_NAME = "assignment";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    AssignmentService assignmentService;
    /**
     * {@code POST  /assignments} : Create a new assignment.
     *
     * @param assignmentDTO the assignmentDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new assignmentDTO, or with status {@code 400 (Bad Request)} if the assignment has already an ID.
     */
    @POST
    public Response createAssignment(@Valid AssignmentDTO assignmentDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Assignment : {}", assignmentDTO);
        if (assignmentDTO.id != null) {
            throw new BadRequestAlertException("A new assignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = assignmentService.persistOrUpdate(assignmentDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /assignments} : Updates an existing assignment.
     *
     * @param assignmentDTO the assignmentDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated assignmentDTO,
     * or with status {@code 400 (Bad Request)} if the assignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assignmentDTO couldn't be updated.
     */
    @PUT
    public Response updateAssignment(@Valid AssignmentDTO assignmentDTO) {
        log.debug("REST request to update Assignment : {}", assignmentDTO);
        if (assignmentDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = assignmentService.persistOrUpdate(assignmentDTO);
        var response = Response.ok().entity(assignmentDTO);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assignmentDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /assignments/:id} : delete the "id" assignment.
     *
     * @param id the id of the assignmentDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteAssignment(@PathParam("id") String id) {
        log.debug("REST request to delete Assignment : {}", id);
        assignmentService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /assignments} : get all the assignments.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of assignments in body.
     */
    @GET
    public Response getAllAssignments(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Assignments");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<AssignmentDTO> result = assignmentService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }


    /**
     * {@code GET  /assignments/:id} : get the "id" assignment.
     *
     * @param id the id of the assignmentDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the assignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getAssignment(@PathParam("id") String id) {
        log.debug("REST request to get Assignment : {}", id);
        Optional<AssignmentDTO> assignmentDTO = assignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assignmentDTO);
    }

    @GET
    @Path("/specific")
    public Response getAllAssignmentByOrganisation(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders) throws AccountNotFoundException {
        log.debug("REST request to get a page of Assignments by Organisation");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        String organisationId = RequestHeaderUtil.getOrganisationId(httpHeaders);
        Paged<AssignmentEmployeeCountDTO> result = assignmentService.findAllByOrganisation(organisationId, page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    @GET
    @Path("/minimal/specific")
    @RolesAllowed({AuthoritiesConstants.MANAGER, AuthoritiesConstants.OWNER})
    public Response getMinimalAllAssignmentByOrganisation(@Context HttpHeaders httpHeaders) throws AccountNotFoundException {
        log.debug("REST request to get all minimal Assignments by Organisation");
        String organisationId = RequestHeaderUtil.getOrganisationId(httpHeaders);
        List<AssignmentDTO> result = assignmentService.findAllByOrganisation(organisationId);
        return Response.ok(result).build();
    }
}
