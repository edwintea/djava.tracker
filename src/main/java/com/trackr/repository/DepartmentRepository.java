package com.trackr.repository;

import com.trackr.domain.Department;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hibernate PanacheMongoDB repository for the Department entity.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class DepartmentRepository implements PanacheMongoRepository<Department> {

    public List<Department> findAllByOrganisation(String organisationId) {
        return find("organisation = ?1", organisationId).stream().collect(Collectors.toList());
    }

}
