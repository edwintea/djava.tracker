package com.trackr.repository;

import com.trackr.domain.Employee;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Hibernate PanacheMongoDB repository for the Employee entity.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class EmployeeRepository implements PanacheMongoRepository<Employee> {

    public Optional<Employee> findOneByUserId(String userId) {
        return find("user = ?1", userId).firstResultOptional();
    }

    public List<Employee> findAllByOrganisation(String organisationId) {
        return find("organisation = ?1", organisationId).stream().collect(Collectors.toList());
    }

    public PanacheQuery<Employee> findPageableEmployeeByOrganisation(String organisationId, Page page) {
        return find("organisation = ?1", Sort.by("fullName", Sort.Direction.Ascending), organisationId).page(page);
    }

    public Long countAllByClient(String clientId) {
        return find("assignmentClient = ?1", clientId).count();
    }

    public Long countAllByAssignment(String assignmentId) {
        return find("assignment = ?1", assignmentId).count();
    }

    public Long countAllByPosition(String positionId) {
        return find("position = ?1", positionId).count();
    }

}
