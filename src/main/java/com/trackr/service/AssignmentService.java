package com.trackr.service;

import com.trackr.domain.Assignment;
import com.trackr.repository.AssignmentRepository;
import com.trackr.service.dto.AssignmentDTO;
import com.trackr.service.dto.AssignmentEmployeeCountDTO;
import com.trackr.service.dto.ClientDTO;
import com.trackr.service.mapper.AssignmentMapper;
import io.quarkus.panache.common.Page;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class AssignmentService {

    private final Logger log = LoggerFactory.getLogger(AssignmentService.class);

    @Inject
    AssignmentRepository assignmentRepository;

    @Inject
    AssignmentMapper assignmentMapper;

    @Inject
    ClientService clientService;

    @Inject
    EmployeeAssignmentService employeeAssignmentService;

    public AssignmentDTO persistOrUpdate(AssignmentDTO assignmentDTO) {
        log.debug("Request to save Assignment : {}", assignmentDTO);
        ClientDTO client = clientService.findOne(assignmentDTO.client).orElseThrow();
        assignmentDTO.clientName = client.name;
        var assignment = assignmentMapper.toEntity(assignmentDTO);
        assignmentRepository.persistOrUpdate(assignment);
        return assignmentMapper.toDto(assignment);
    }

    /**
     * Delete the Assignment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Assignment : {}", id);
        assignmentRepository.findByIdOptional(new ObjectId(id)).ifPresent(assignment -> {
            assignmentRepository.delete(assignment);
        });
    }

    /**
     * Get one assignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<AssignmentDTO> findOne(String id) {
        log.debug("Request to get Assignment : {}", id);
        return assignmentRepository.findByIdOptional(new ObjectId(id))
            .map(assignment -> assignmentMapper.toDto((Assignment) assignment));
    }

    /**
     * Get all the assignments.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<AssignmentDTO> findAll(Page page) {
        log.debug("Request to get all Assignments");
        return new Paged<>(assignmentRepository.findAll().page(page))
            .map(assignment -> assignmentMapper.toDto((Assignment) assignment));
    }

    public Paged<AssignmentEmployeeCountDTO> findAllByOrganisation(String organisationId, Page page) {
        log.debug("Request to get all Assignment");
        return new Paged<>(assignmentRepository.findPageableAssignmentByOrganisation(organisationId, page))
            .map(assignment -> new AssignmentEmployeeCountDTO(assignmentMapper.toDto(assignment), employeeAssignmentService.countAllByAssignment(assignment.id.toString())));
    }

    public List<AssignmentDTO> findAllByOrganisation(String organisationId) {
        log.debug("Request to get all Assignment");
        List<AssignmentDTO> assignments = assignmentRepository.findAssignmentByOrganisation(organisationId)
            .stream().map(assignment -> assignmentMapper.toDto(assignment)).collect(Collectors.toList());
        Collections.reverse(assignments);
        return assignments;
    }



}
