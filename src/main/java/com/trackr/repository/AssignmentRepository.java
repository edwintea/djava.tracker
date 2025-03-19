package com.trackr.repository;

import com.trackr.domain.Assignment;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;

/**
 * Hibernate PanacheMongoDB repository for the Assignment entity.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class AssignmentRepository implements PanacheMongoRepository<Assignment> {

    public PanacheQuery<Assignment> findPageableAssignmentByOrganisation(String organisationId, Page page) {
        return find("organisation = ?1", organisationId).page(page);
    }

    public PanacheQuery<Assignment> findAssignmentByOrganisation(String organisationId) {
        return find("organisation = ?1", organisationId);
    }

}
