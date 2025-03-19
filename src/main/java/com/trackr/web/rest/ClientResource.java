package com.trackr.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.trackr.security.AuthoritiesConstants;
import com.trackr.service.ClientService;
import com.trackr.repository.ClientRepository;
import com.trackr.service.dto.ClientEmployeeCountDTO;
import com.trackr.service.dto.ClientMinimalDTO;
import com.trackr.web.rest.errors.BadRequestAlertException;
import com.trackr.web.util.HeaderUtil;
import com.trackr.web.util.RequestHeaderUtil;
import com.trackr.web.util.ResponseUtil;
import com.trackr.service.dto.ClientDTO;

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
 * REST controller for managing {@link com.trackr.domain.Client}.
 */
@Path("/api/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private static final String ENTITY_NAME = "client";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    ClientService clientService;
    /**
     * {@code POST  /clients} : Create a new client.
     *
     * @param clientDTO the clientDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new clientDTO, or with status {@code 400 (Bad Request)} if the client has already an ID.
     */
    @POST
    @RolesAllowed({AuthoritiesConstants.MANAGER, AuthoritiesConstants.OWNER})
    public Response createClient(@Context HttpHeaders httpHeaders, @Valid ClientDTO clientDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Client : {}", clientDTO);
        if (clientDTO.id != null) {
            throw new BadRequestAlertException("A new client cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = clientService.persistOrUpdate(clientDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /clients} : Updates an existing client.
     *
     * @param clientDTO the clientDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated clientDTO,
     * or with status {@code 400 (Bad Request)} if the clientDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientDTO couldn't be updated.
     */
    @PUT
    public Response updateClient(@Valid ClientDTO clientDTO) {
        log.debug("REST request to update Client : {}", clientDTO);
        if (clientDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = clientService.persistOrUpdate(clientDTO);
        var response = Response.ok().entity(clientDTO);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clientDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /clients/:id} : delete the "id" client.
     *
     * @param id the id of the clientDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteClient(@PathParam("id") String id) {
        log.debug("REST request to delete Client : {}", id);
        clientService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /clients} : get all the clients.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of clients in body.
     */
    @GET
    public Response getAllClients(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Clients");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<ClientDTO> result = clientService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }


    /**
     * {@code GET  /clients/:id} : get the "id" client.
     *
     * @param id the id of the clientDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the clientDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getClient(@PathParam("id") String id) {
        log.debug("REST request to get Client : {}", id);
        Optional<ClientDTO> clientDTO = clientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clientDTO);
    }

    @GET
    @Path("/minimal/specific")
    public Response getMinimalSpecific(@Context HttpHeaders httpHeaders) throws AccountNotFoundException {
        log.debug("REST request to get all Client (minimal) by Organisation");
        String organisationId = RequestHeaderUtil.getOrganisationId(httpHeaders);
        List<ClientMinimalDTO> clients = clientService.findAllMinimalByOrganisation(organisationId);
        return Response.ok(clients).build();
    }

    @GET
    @Path("/specific")
    @RolesAllowed({AuthoritiesConstants.MANAGER, AuthoritiesConstants.OWNER})
    public Response getAllClients(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders) throws AccountNotFoundException {
        log.debug("REST request to get a page of Clients by Organisation");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        String organisationId = RequestHeaderUtil.getOrganisationId(httpHeaders);
        Paged<ClientEmployeeCountDTO> result = clientService.findAllByOrganisation(organisationId, page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }
}
