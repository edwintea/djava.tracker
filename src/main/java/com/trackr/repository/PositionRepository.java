package com.trackr.repository;

import com.trackr.domain.Position;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hibernate PanacheMongoDB repository for the Position entity.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class PositionRepository implements PanacheMongoRepository<Position> {

    public List<Position> findPositionByOrganisation(String organisationId) {
        return find("organisation = ?1", Sort.by("departmentName", Sort.Direction.Ascending).and("name", Sort.Direction.Ascending), organisationId).stream().collect(Collectors.toList());
    }

    public PanacheQuery<Position> findPageablePositionByOrganisation(String organisationId, Page page) {
        return find("organisation = ?1", Sort.by("departmentName", Sort.Direction.Ascending).and("name", Sort.Direction.Ascending), organisationId).page(page);
    }

}
