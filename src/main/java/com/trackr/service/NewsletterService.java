package com.trackr.service;

import com.trackr.domain.Newsletter;
import com.trackr.repository.NewsletterRepository;
import com.trackr.service.dto.NewsletterDTO;
import com.trackr.service.mapper.NewsletterMapper;
import io.quarkus.panache.common.Page;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class NewsletterService {

    private final Logger log = LoggerFactory.getLogger(NewsletterService.class);

    @Inject
    NewsletterRepository newsletterRepository;

    @Inject
    NewsletterMapper newsletterMapper;

    public NewsletterDTO persistOrUpdate(NewsletterDTO newsletterDTO) {
        log.debug("Request to save Newsletter : {}", newsletterDTO);
        var newsletter = newsletterMapper.toEntity(newsletterDTO);
        newsletterRepository.persistOrUpdate(newsletter);
        return newsletterMapper.toDto(newsletter);
    }

    /**
     * Delete the Newsletter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Newsletter : {}", id);
        newsletterRepository.findByIdOptional(new ObjectId(id)).ifPresent(newsletter -> {
            newsletterRepository.delete(newsletter);
        });
    }

    /**
     * Get one newsletter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<NewsletterDTO> findOne(String id) {
        log.debug("Request to get Newsletter : {}", id);
        return newsletterRepository.findByIdOptional(new ObjectId(id))
            .map(newsletter -> newsletterMapper.toDto((Newsletter) newsletter));
    }

    /**
     * Get all the newsletters.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<NewsletterDTO> findAll(Page page) {
        log.debug("Request to get all Newsletters");
        return new Paged<>(newsletterRepository.findAll().page(page))
            .map(newsletter -> newsletterMapper.toDto((Newsletter) newsletter));
    }

    public Paged<NewsletterDTO> findAllByOrganisation(String organisationId, Page page) {
        log.debug("Request to get all Newsletter");
        return new Paged<>(newsletterRepository.findPageableNewsletterByOrganisation(organisationId, page))
            .map(newsletter -> newsletterMapper.toDto(newsletter));
    }



}
