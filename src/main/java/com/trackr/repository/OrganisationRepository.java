package com.trackr.repository;

import com.trackr.domain.Organisation;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

/**
 * Hibernate PanacheMongoDB repository for the Organisation entity.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class OrganisationRepository implements PanacheMongoRepository<Organisation> {

    public Optional<Organisation> findOneByOrgCode(String code) {
        return find("joinCode = ?1", code).firstResultOptional();
    }

}
