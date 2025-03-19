package com.trackr.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.trackr.service.AnnualLeaveService;
import com.trackr.repository.AnnualLeaveRepository;
import com.trackr.web.rest.errors.BadRequestAlertException;
import com.trackr.web.util.HeaderUtil;
import com.trackr.web.util.ResponseUtil;
import com.trackr.service.dto.AnnualLeaveDTO;

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
 * REST controller for managing {@link com.trackr.domain.AnnualLeave}.
 */
@Path("/api/annual-leaves")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AnnualLeaveResource {

    private final Logger log = LoggerFactory.getLogger(AnnualLeaveResource.class);

    private static final String ENTITY_NAME = "annualLeave";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    AnnualLeaveService annualLeaveService;
    /**
     * {@code POST  /annual-leaves} : Create a new annualLeave.
     *
     * @param annualLeaveDTO the annualLeaveDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new annualLeaveDTO, or with status {@code 400 (Bad Request)} if the annualLeave has already an ID.
     */
    @POST
    public Response createAnnualLeave(@Valid AnnualLeaveDTO annualLeaveDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save AnnualLeave : {}", annualLeaveDTO);
        if (annualLeaveDTO.id != null) {
            throw new BadRequestAlertException("A new annualLeave cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = annualLeaveService.persistOrUpdate(annualLeaveDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /annual-leaves} : Updates an existing annualLeave.
     *
     * @param annualLeaveDTO the annualLeaveDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated annualLeaveDTO,
     * or with status {@code 400 (Bad Request)} if the annualLeaveDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annualLeaveDTO couldn't be updated.
     */
    @PUT
    public Response updateAnnualLeave(@Valid AnnualLeaveDTO annualLeaveDTO) {
        log.debug("REST request to update AnnualLeave : {}", annualLeaveDTO);
        if (annualLeaveDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = annualLeaveService.persistOrUpdate(annualLeaveDTO);
        var response = Response.ok().entity(annualLeaveDTO);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, annualLeaveDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /annual-leaves/:id} : delete the "id" annualLeave.
     *
     * @param id the id of the annualLeaveDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteAnnualLeave(@PathParam("id") String id) {
        log.debug("REST request to delete AnnualLeave : {}", id);
        annualLeaveService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /annual-leaves} : get all the annualLeaves.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of annualLeaves in body.
     */
    @GET
    public Response getAllAnnualLeaves(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of AnnualLeaves");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<AnnualLeaveDTO> result = annualLeaveService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }


    /**
     * {@code GET  /annual-leaves/:id} : get the "id" annualLeave.
     *
     * @param id the id of the annualLeaveDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the annualLeaveDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getAnnualLeave(@PathParam("id") String id) {
        log.debug("REST request to get AnnualLeave : {}", id);
        Optional<AnnualLeaveDTO> annualLeaveDTO = annualLeaveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(annualLeaveDTO);
    }
}
