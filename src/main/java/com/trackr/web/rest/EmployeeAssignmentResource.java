package com.trackr.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.trackr.service.EmployeeAssignmentService;
import com.trackr.repository.EmployeeAssignmentRepository;
import com.trackr.web.rest.errors.BadRequestAlertException;
import com.trackr.web.util.HeaderUtil;
import com.trackr.web.util.ResponseUtil;
import com.trackr.service.dto.EmployeeAssignmentDTO;

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
 * REST controller for managing {@link com.trackr.domain.EmployeeAssignment}.
 */
@Path("/api/employee-assignments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class EmployeeAssignmentResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeAssignmentResource.class);

    private static final String ENTITY_NAME = "employeeAssignment";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    EmployeeAssignmentService employeeAssignmentService;
    /**
     * {@code POST  /employee-assignments} : Create a new employeeAssignment.
     *
     * @param employeeAssignmentDTO the employeeAssignmentDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new employeeAssignmentDTO, or with status {@code 400 (Bad Request)} if the employeeAssignment has already an ID.
     */
    @POST
    public Response createEmployeeAssignment(@Valid EmployeeAssignmentDTO employeeAssignmentDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save EmployeeAssignment : {}", employeeAssignmentDTO);
        if (employeeAssignmentDTO.id != null) {
            throw new BadRequestAlertException("A new employeeAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = employeeAssignmentService.persistOrUpdate(employeeAssignmentDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /employee-assignments} : Updates an existing employeeAssignment.
     *
     * @param employeeAssignmentDTO the employeeAssignmentDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated employeeAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the employeeAssignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeAssignmentDTO couldn't be updated.
     */
    @PUT
    public Response updateEmployeeAssignment(@Valid EmployeeAssignmentDTO employeeAssignmentDTO) {
        log.debug("REST request to update EmployeeAssignment : {}", employeeAssignmentDTO);
        if (employeeAssignmentDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = employeeAssignmentService.persistOrUpdate(employeeAssignmentDTO);
        var response = Response.ok().entity(employeeAssignmentDTO);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, employeeAssignmentDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /employee-assignments/:id} : delete the "id" employeeAssignment.
     *
     * @param id the id of the employeeAssignmentDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteEmployeeAssignment(@PathParam("id") String id) {
        log.debug("REST request to delete EmployeeAssignment : {}", id);
        employeeAssignmentService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /employee-assignments} : get all the employeeAssignments.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of employeeAssignments in body.
     */
    @GET
    public Response getAllEmployeeAssignments(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of EmployeeAssignments");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<EmployeeAssignmentDTO> result = employeeAssignmentService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }


    /**
     * {@code GET  /employee-assignments/:id} : get the "id" employeeAssignment.
     *
     * @param id the id of the employeeAssignmentDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the employeeAssignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getEmployeeAssignment(@PathParam("id") String id) {
        log.debug("REST request to get EmployeeAssignment : {}", id);
        Optional<EmployeeAssignmentDTO> employeeAssignmentDTO = employeeAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeAssignmentDTO);
    }

    @GET
    @Path("/specific/{id}")
    public Response getEmployeeAssignmentByUser(@PathParam("id") String id) {
        log.debug("REST request to get EmployeeAssignment by User : {}", id);
        List<EmployeeAssignmentDTO> employeeAssignmentDTO = employeeAssignmentService.findAllByUser(id);
        return Response.ok(employeeAssignmentDTO).build();
    }
}
