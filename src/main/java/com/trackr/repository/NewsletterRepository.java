package com.trackr.repository;

import com.trackr.domain.Newsletter;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;

/**
 * Hibernate PanacheMongoDB repository for the Newsletter entity.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class NewsletterRepository implements PanacheMongoRepository<Newsletter> {

    public PanacheQuery<Newsletter> findPageableNewsletterByOrganisation(String organisationId, Page page) {
        return find("organisation = ?1", Sort.by("createdDate", Sort.Direction.Descending), organisationId).page(page);
    }

}
