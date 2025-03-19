package com.trackr.repository;

import com.trackr.domain.Client;
import com.trackr.domain.Organisation;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Hibernate PanacheMongoDB repository for the Client entity.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class ClientRepository implements PanacheMongoRepository<Client> {

    public List<Client> findAllByOrganisation(String organisationId) {
        return find("organisation = ?1", organisationId).stream().collect(Collectors.toList());
    }

    public PanacheQuery<Client> findPageableClientByOrganisation(String organisationId, Page page) {
        return find("organisation = ?1", Sort.by("name", Sort.Direction.Ascending), organisationId).page(page);
    }

}
