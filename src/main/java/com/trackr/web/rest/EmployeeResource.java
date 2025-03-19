package com.trackr.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.trackr.security.AuthoritiesConstants;
import com.trackr.service.EmployeeService;
import com.trackr.repository.EmployeeRepository;
import com.trackr.web.rest.errors.BadRequestAlertException;
import com.trackr.web.util.HeaderUtil;
import com.trackr.web.util.RequestHeaderUtil;
import com.trackr.web.util.ResponseUtil;
import com.trackr.service.dto.EmployeeDTO;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.trackr.service.Paged;
import com.trackr.web.rest.vm.PageRequestVM;
import com.trackr.web.rest.vm.SortRequestVM;
import com.trackr.web.util.PaginationUtil;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.trackr.domain.Employee}.
 */
@Path("/api/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class EmployeeResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);

    private static final String ENTITY_NAME = "employee";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    EmployeeService employeeService;
    /**
     * {@code POST  /employees} : Create a new employee.
     *
     * @param employeeDTO the employeeDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new employeeDTO, or with status {@code 400 (Bad Request)} if the employee has already an ID.
     */
    @POST
    public Response createEmployee(@Valid EmployeeDTO employeeDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Employee : {}", employeeDTO);
        if (employeeDTO.id != null) {
            throw new BadRequestAlertException("A new employee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = employeeService.persistOrUpdate(employeeDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /employees} : Updates an existing employee.
     *
     * @param employeeDTO the employeeDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated employeeDTO,
     * or with status {@code 400 (Bad Request)} if the employeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeDTO couldn't be updated.
     */
    @PUT
    public Response updateEmployee(@Valid EmployeeDTO employeeDTO) {
        log.debug("REST request to update Employee : {}", employeeDTO);
        if (employeeDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = employeeService.persistOrUpdate(employeeDTO);
        var response = Response.ok().entity(employeeDTO);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, employeeDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /employees/:id} : delete the "id" employee.
     *
     * @param id the id of the employeeDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteEmployee(@PathParam("id") String id) {
        log.debug("REST request to delete Employee : {}", id);
        employeeService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /employees} : get all the employees.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of employees in body.
     */
    @GET
    public Response getAllEmployees(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Employees");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<EmployeeDTO> result = employeeService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }


    /**
     * {@code GET  /employees/:id} : get the "id" employee.
     *
     * @param id the id of the employeeDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the employeeDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getEmployee(@PathParam("id") String id) {
        log.debug("REST request to get Employee : {}", id);
        Optional<EmployeeDTO> employeeDTO = employeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeDTO);
    }

    @GET
    @Path("/specific")
    @RolesAllowed({AuthoritiesConstants.MANAGER, AuthoritiesConstants.OWNER})
    public Response getAllEmployeesByOrganisation(@Context HttpHeaders httpHeaders, @BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) throws AccountNotFoundException {
        log.debug("REST request to get a page of Employees by Organisation");
        var page = pageRequest.toPage();
        String organisationId = RequestHeaderUtil.getOrganisationId(httpHeaders);
        Paged<EmployeeDTO> result = employeeService.findAllByOrganisation(organisationId, page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }
}
