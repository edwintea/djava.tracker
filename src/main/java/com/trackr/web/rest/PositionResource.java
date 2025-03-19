package com.trackr.web.rest;

import com.trackr.security.AuthoritiesConstants;
import com.trackr.service.Paged;
import com.trackr.service.PositionService;
import com.trackr.service.dto.PositionDTO;
import com.trackr.service.dto.PositionEmployeeCountDTO;
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
 * REST controller for managing {@link com.trackr.domain.Position}.
 */
@Path("/api/positions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PositionResource {

    private final Logger log = LoggerFactory.getLogger(PositionResource.class);

    private static final String ENTITY_NAME = "position";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    PositionService positionService;
    /**
     * {@code POST  /positions} : Create a new position.
     *
     * @param positionDTO the positionDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new positionDTO, or with status {@code 400 (Bad Request)} if the position has already an ID.
     */
    @POST
    public Response createPosition(@Valid PositionDTO positionDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Position : {}", positionDTO);
        if (positionDTO.id != null) {
            throw new BadRequestAlertException("A new position cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = positionService.persistOrUpdate(positionDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /positions} : Updates an existing position.
     *
     * @param positionDTO the positionDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated positionDTO,
     * or with status {@code 400 (Bad Request)} if the positionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the positionDTO couldn't be updated.
     */
    @PUT
    public Response updatePosition(@Valid PositionDTO positionDTO) {
        log.debug("REST request to update Position : {}", positionDTO);
        if (positionDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = positionService.persistOrUpdate(positionDTO);
        var response = Response.ok().entity(positionDTO);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, positionDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /positions/:id} : delete the "id" position.
     *
     * @param id the id of the positionDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deletePosition(@PathParam("id") String id) {
        log.debug("REST request to delete Position : {}", id);
        positionService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /positions} : get all the positions.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of positions in body.
     */
    @GET
    public Response getAllPositions(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Positions");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<PositionDTO> result = positionService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }


    /**
     * {@code GET  /positions/:id} : get the "id" position.
     *
     * @param id the id of the positionDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the positionDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getPosition(@PathParam("id") String id) {
        log.debug("REST request to get Position : {}", id);
        Optional<PositionDTO> positionDTO = positionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(positionDTO);
    }

    @GET
    @Path("/specific")
    @RolesAllowed({AuthoritiesConstants.MANAGER, AuthoritiesConstants.OWNER})
    public Response getAllPositionsByOrganisation(@Context HttpHeaders httpHeaders) throws AccountNotFoundException {
        log.debug("REST request to get a page of Positions by Organisation");
        String organisationId = RequestHeaderUtil.getOrganisationId(httpHeaders);
        List<PositionEmployeeCountDTO> result = positionService.findAllByPosition(organisationId);
        return Response.ok(result).build();
    }
}
