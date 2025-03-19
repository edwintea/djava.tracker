package com.trackr.web.rest;

import com.trackr.security.AuthoritiesConstants;
import com.trackr.service.OrganisationService;
import com.trackr.service.Paged;
import com.trackr.service.dto.DashboardComboDTO;
import com.trackr.service.dto.OrganisationBaseDTO;
import com.trackr.service.dto.OrganisationDTO;
import com.trackr.service.dto.OrganisationMinimalDTO;
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
import java.util.Optional;

import static javax.ws.rs.core.UriBuilder.fromPath;

/**
 * REST controller for managing {@link com.trackr.domain.Organisation}.
 */
@Path("/api/organisations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class OrganisationResource {

    private final Logger log = LoggerFactory.getLogger(OrganisationResource.class);

    private static final String ENTITY_NAME = "organisation";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    OrganisationService organisationService;
    /**
     * {@code POST  /organisations} : Create a new organisation.
     *
     * @param organisationDTO the organisationDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new organisationDTO, or with status {@code 400 (Bad Request)} if the organisation has already an ID.
     */
    @POST
    public Response createOrganisation(@Valid OrganisationDTO organisationDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Organisation : {}", organisationDTO);
        if (organisationDTO.id != null) {
            throw new BadRequestAlertException("A new organisation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = organisationService.persistOrUpdate(organisationDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /organisations} : Updates an existing organisation.
     *
     * @param organisationDTO the organisationDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated organisationDTO,
     * or with status {@code 400 (Bad Request)} if the organisationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organisationDTO couldn't be updated.
     */
    @PUT
    public Response updateOrganisation(@Valid OrganisationDTO organisationDTO) {
        log.debug("REST request to update Organisation : {}", organisationDTO);
        if (organisationDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = organisationService.persistOrUpdate(organisationDTO);
        var response = Response.ok().entity(organisationDTO);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, organisationDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /organisations/:id} : delete the "id" organisation.
     *
     * @param id the id of the organisationDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteOrganisation(@PathParam("id") String id) {
        log.debug("REST request to delete Organisation : {}", id);
        organisationService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /organisations} : get all the organisations.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of organisations in body.
     */
    @GET
    public Response getAllOrganisations(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Organisations");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<OrganisationDTO> result = organisationService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }


    /**
     * {@code GET  /organisations/:id} : get the "id" organisation.
     *
     * @param id the id of the organisationDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the organisationDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getOrganisation(@PathParam("id") String id) {
        log.debug("REST request to get Organisation : {}", id);
        Optional<OrganisationDTO> organisationDTO = organisationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organisationDTO);
    }

    @GET
    @Path("/joinCode/{code}")
    public Response getOrganisationByCode(@PathParam("code") String code) {
        log.debug("REST request to get Organisation by join code : {}", code);
        Optional<OrganisationMinimalDTO> organisationDTO = organisationService.findByCodeMinimal(code);
        return ResponseUtil.wrapOrNotFound(organisationDTO);
    }

    @GET
    @Path("/admin/dashboard")
    @RolesAllowed({AuthoritiesConstants.MANAGER, AuthoritiesConstants.OWNER})
    public Response getOrganisationDashboard(@Context HttpHeaders httpHeaders) throws AccountNotFoundException {
        log.debug("REST request to get Organisation admin dashboard ");

        String organisationId = RequestHeaderUtil.getOrganisationId(httpHeaders);
        DashboardComboDTO dashboardCombo = organisationService.getOrganisationDashboard(organisationId);
        return Response.ok().entity(dashboardCombo).build();
    }

    @GET
    @Path("/base")
    @RolesAllowed({AuthoritiesConstants.MANAGER, AuthoritiesConstants.OWNER})
    public Response getOrganisationBase(@Context HttpHeaders httpHeaders) throws AccountNotFoundException {
        log.debug("REST request to get Organisation admin dashboard ");

        String organisationId = RequestHeaderUtil.getOrganisationId(httpHeaders);
        OrganisationBaseDTO organisationBase = organisationService.getOrganisationBase(organisationId);
        return Response.ok().entity(organisationBase).build();
    }
}
