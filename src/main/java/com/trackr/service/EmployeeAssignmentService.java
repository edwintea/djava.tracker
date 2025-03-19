package com.trackr.service;

import com.trackr.domain.Employee;
import com.trackr.domain.enumeration.AssignmentType;
import io.quarkus.panache.common.Page;
import com.trackr.domain.EmployeeAssignment;
import com.trackr.repository.EmployeeAssignmentRepository;
import com.trackr.service.dto.EmployeeAssignmentDTO;
import com.trackr.service.mapper.EmployeeAssignmentMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmployeeAssignmentService {

    private final Logger log = LoggerFactory.getLogger(EmployeeAssignmentService.class);

    @Inject
    EmployeeAssignmentRepository employeeAssignmentRepository;

    @Inject
    EmployeeAssignmentMapper employeeAssignmentMapper;

    public EmployeeAssignmentDTO persistOrUpdate(EmployeeAssignmentDTO employeeAssignmentDTO) {
        log.debug("Request to save EmployeeAssignment : {}", employeeAssignmentDTO);
        var employeeAssignment = employeeAssignmentMapper.toEntity(employeeAssignmentDTO);
        employeeAssignmentRepository.persistOrUpdate(employeeAssignment);
        return employeeAssignmentMapper.toDto(employeeAssignment);
    }

    /**
     * Delete the EmployeeAssignment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete EmployeeAssignment : {}", id);
        employeeAssignmentRepository.findByIdOptional(new ObjectId(id)).ifPresent(employeeAssignment -> {
            employeeAssignmentRepository.delete(employeeAssignment);
        });
    }

    /**
     * Get one employeeAssignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<EmployeeAssignmentDTO> findOne(String id) {
        log.debug("Request to get EmployeeAssignment : {}", id);
        return employeeAssignmentRepository.findByIdOptional(new ObjectId(id))
            .map(employeeAssignment -> employeeAssignmentMapper.toDto((EmployeeAssignment) employeeAssignment));
    }

    /**
     * Get all the employeeAssignments.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<EmployeeAssignmentDTO> findAll(Page page) {
        log.debug("Request to get all EmployeeAssignments");
        return new Paged<>(employeeAssignmentRepository.findAll().page(page))
            .map(employeeAssignment -> employeeAssignmentMapper.toDto((EmployeeAssignment) employeeAssignment));
    }

    public List<EmployeeAssignmentDTO> findAllByUser(String userId) {
        log.debug("Request to get all EmployeeAssignments by User");
        return employeeAssignmentRepository.findAllByUser(userId)
            .stream().map(user -> employeeAssignmentMapper.toDto(user)).collect(Collectors.toList());
    }

    public List<EmployeeAssignmentDTO> findAllByAssignment(String assignmentId) {
        log.debug("Request to get all EmployeeAssignments by Assignment");
        return employeeAssignmentRepository.findAllByAssignment(assignmentId)
            .stream().map(assignment -> employeeAssignmentMapper.toDto(assignment)).collect(Collectors.toList());
    }

    public Optional<EmployeeAssignmentDTO> findMainByUser(String userId) {
        log.debug("Request to get all EmployeeAssignments by Assignment");
        return employeeAssignmentRepository.findMainByUser(userId)
            .map(employeeAssignment -> employeeAssignmentMapper.toDto(employeeAssignment));
    }

    public Long countAllByAssignment(String assignmentId) {
        log.debug("Request to count all EmployeeAssignments by Assignment");
        return this.findAllByAssignment(assignmentId).stream().count();
    }

    public Long countAllMainByAssignmentType(AssignmentType assignmentType) {
        log.debug("Request to count all EmployeeAssignments by Assignment type");
        return employeeAssignmentRepository.findAllMainByAssignmentType(assignmentType);
    }

    public Long countAllMainByClient(String clientId) {
        log.debug("Request to count all EmployeeAssignments by Assignment type");
        return employeeAssignmentRepository.findAllMainByClient(clientId);
    }



}
