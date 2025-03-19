package com.trackr.repository;

import com.trackr.domain.EmployeeAssignment;
import com.trackr.domain.enumeration.AssignmentType;
import io.quarkus.mongodb.panache.PanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Hibernate PanacheMongoDB repository for the EmployeeAssignment entity.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class EmployeeAssignmentRepository implements PanacheMongoRepository<EmployeeAssignment> {

    public List<EmployeeAssignment> findAllByUser(String userId) {
        return find("user = ?1", userId).stream().collect(Collectors.toList());
    }

    public List<EmployeeAssignment> findAllByClient(String clientId) {
        return find("client = ?1", clientId).stream().collect(Collectors.toList());
    }

    public List<EmployeeAssignment> findAllByAssignment(String assignmentId) {
        return find("assignment = ?1 and main = ?2", assignmentId, true).stream().collect(Collectors.toList());
    }

    public Optional<EmployeeAssignment> findMainByUser(String userId) {
        return find("user = ?1 and main = ?2", userId, true).stream().findFirst();
    }

    public Long findAllMainByAssignmentType(AssignmentType assignmentType) {
        return find("assignmentType = ?1 and main = ?2", assignmentType, true).count();
    }

    public Long findAllMainByClient(String clientId) {
        return find("client = ?1 and main = ?2", clientId, true).count();
    }

}
