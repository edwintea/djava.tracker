package com.trackr.web.rest;

import com.trackr.service.NewsletterService;
import com.trackr.service.Paged;
import com.trackr.service.dto.NewsletterDTO;
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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Optional;

import static javax.ws.rs.core.UriBuilder.fromPath;

/**
 * REST controller for managing {@link com.trackr.domain.Newsletter}.
 */
@Path("/api/newsletters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class NewsletterResource {

    private final Logger log = LoggerFactory.getLogger(NewsletterResource.class);

    private static final String ENTITY_NAME = "newsletter";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    NewsletterService newsletterService;
    /**
     * {@code POST  /newsletters} : Create a new newsletter.
     *
     * @param newsletterDTO the newsletterDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new newsletterDTO, or with status {@code 400 (Bad Request)} if the newsletter has already an ID.
     */
    @POST
    public Response createNewsletter(@Valid NewsletterDTO newsletterDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Newsletter : {}", newsletterDTO);
        if (newsletterDTO.id != null) {
            throw new BadRequestAlertException("A new newsletter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = newsletterService.persistOrUpdate(newsletterDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /newsletters} : Updates an existing newsletter.
     *
     * @param newsletterDTO the newsletterDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated newsletterDTO,
     * or with status {@code 400 (Bad Request)} if the newsletterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the newsletterDTO couldn't be updated.
     */
    @PUT
    public Response updateNewsletter(@Valid NewsletterDTO newsletterDTO) {
        log.debug("REST request to update Newsletter : {}", newsletterDTO);
        if (newsletterDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = newsletterService.persistOrUpdate(newsletterDTO);
        var response = Response.ok().entity(newsletterDTO);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, newsletterDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /newsletters/:id} : delete the "id" newsletter.
     *
     * @param id the id of the newsletterDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteNewsletter(@PathParam("id") String id) {
        log.debug("REST request to delete Newsletter : {}", id);
        newsletterService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /newsletters} : get all the newsletters.
     *
     * @param pageRequest the pagination information.
     * @return the {@link Response} with status {@code 200 (OK)} and the list of newsletters in body.
     */
    @GET
    public Response getAllNewsletters(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo) {
        log.debug("REST request to get a page of Newsletters");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<NewsletterDTO> result = newsletterService.findAll(page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }


    /**
     * {@code GET  /newsletters/:id} : get the "id" newsletter.
     *
     * @param id the id of the newsletterDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the newsletterDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getNewsletter(@PathParam("id") String id) {
        log.debug("REST request to get Newsletter : {}", id);
        Optional<NewsletterDTO> newsletterDTO = newsletterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(newsletterDTO);
    }

    @GET
    @Path("/specific")
    public Response getAllNewsletterByOrganisation(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo, @Context HttpHeaders httpHeaders) throws AccountNotFoundException {
        log.debug("REST request to get a page of Newsletter by Organisation");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        String organisationId = RequestHeaderUtil.getOrganisationId(httpHeaders);
        Paged<NewsletterDTO> result = newsletterService.findAllByOrganisation(organisationId, page);
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }
}
