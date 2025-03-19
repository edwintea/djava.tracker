package com.trackr.web.rest;

import com.trackr.security.AuthoritiesConstants;
import com.trackr.service.DepartmentService;
import com.trackr.service.Paged;
import com.trackr.service.dto.DepartmentDTO;
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
 * REST controller for managing {@link com.trackr.domain.Department}.
 */
@Path("/api/departments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DepartmentResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentResource.class);

    private static final String ENTITY_NAME = "department";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    DepartmentService departmentService;
    /**
     * {@code POST  /departments} : Create a new department.
     *
     * @param departmentDTO the departmentDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new departmentDTO, or with status {@code 400 (Bad Request)} if the department has already an ID.
     */
    @POST
    @RolesAllowed({AuthoritiesConstants.MANAGER, AuthoritiesConstants.OWNER})
    public Response createDepartment(@Valid DepartmentDTO departmentDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Department : {}", departmentDTO);
        if (departmentDTO.id != null) {
            throw new BadRequestAlertException("A new department cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = departmentService.persistOrUpdate(departmentDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /departments} : Updates an existing department.
     *
     * @param departmentDTO the departmentDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated departmentDTO,
     * or with status {@code 400 (Bad Request)} if the departmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentDTO couldn't be updated.
     */
    @PUT
    public Response updateDepartment(@Valid DepartmentDTO departmentDTO) {
        log.debug("REST request to update Department : {}", departmentDTO);
        if (departmentDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = departmentService.persistOrUpdate(departmentDTO);
        var response = Response.ok().entity(departmentDTO);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, departmentDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /departments/:id} : delete the "id" department.
     *
     * @param id the id of the departmentDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteDepartment(@PathParam("id") String id) {
        log.debug("REST request to delete Department : {}", id);
        departmentService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /departments} : get all the departments.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of departments in body.
     */
    @GET
    public Response getAllDepartments(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Departments");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<DepartmentDTO> result = departmentService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    @GET
    @Path("/specific")
    public Response getAllDepartmentsByOrganisation(@Context HttpHeaders httpHeaders) throws AccountNotFoundException {
        log.debug("REST request to get all Departments by Organisation");

        String organisationId = RequestHeaderUtil.getOrganisationId(httpHeaders);
        List<DepartmentDTO> departments = departmentService.findAllByOrganisation(organisationId);
        return Response.ok(departments).build();
    }


    /**
     * {@code GET  /departments/:id} : get the "id" department.
     *
     * @param id the id of the departmentDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the departmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getDepartment(@PathParam("id") String id) {
        log.debug("REST request to get Department : {}", id);
        Optional<DepartmentDTO> departmentDTO = departmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departmentDTO);
    }
}
