package com.trackr.service;

import com.trackr.domain.Employee;
import com.trackr.domain.User;
import com.trackr.repository.EmployeeRepository;
import com.trackr.service.dto.EmployeeDTO;
import com.trackr.service.mapper.EmployeeMapper;
import io.quarkus.panache.common.Page;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    EmployeeMapper employeeMapper;

    @Inject
    EmployeeAssignmentService employeeAssignmentService;

    @Inject
    EmployeeUtilService employeeUtilService;

    public EmployeeDTO persistOrUpdate(EmployeeDTO employeeDTO) {
        log.debug("Request to save Employee : {}", employeeDTO);
        var employee = employeeMapper.toEntity(employeeDTO);
        employeeRepository.persistOrUpdate(employee);
        return employeeMapper.toDto(employee);
    }

    /**
     * Delete the Employee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.findByIdOptional(new ObjectId(id)).ifPresent(employee -> {
            employeeRepository.delete(employee);
        });
    }

    /**
     * Get one employee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<EmployeeDTO> findOne(String id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findByIdOptional(new ObjectId(id))
            .map(employee -> employeeMapper.toDto((Employee) employee));
    }

    /**
     * Get all the employees.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<EmployeeDTO> findAll(Page page) {
        log.debug("Request to get all Employees");
        return new Paged<>(employeeRepository.findAll().page(page))
            .map(employee -> employeeMapper.toDto((Employee) employee));
    }

    public EmployeeDTO createEmployee(String organisationId, User user, Boolean privileged) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        String fullName = user.firstName + " " + user.lastName;
        String userId = user.id.toString();

        employeeDTO = employeeUtilService.populateUserFields(employeeDTO, fullName, userId);
        employeeDTO = employeeUtilService.populateHRFields(employeeDTO, privileged, organisationId);

        this.persistOrUpdate(employeeDTO);

        return employeeDTO;
    }

    public Optional<EmployeeDTO> findOneByUserId(String userId) {
        return employeeRepository.findOneByUserId(userId).map(employee -> employeeMapper.toDto(employee));
    }

    public List<EmployeeDTO> findAllByOrganisation(String organisationId) {
        return employeeRepository.findAllByOrganisation(organisationId).stream().map(employee -> employeeMapper.toDto(employee)).collect(Collectors.toList());
    }

    public Paged<EmployeeDTO> findAllByOrganisation(String organisationId, Page page) {
        log.debug("Request to get all Employees");
        return new Paged<>(employeeRepository.findPageableEmployeeByOrganisation(organisationId, page))
            .map(employee -> {
                EmployeeDTO employeeDTO = employeeMapper.toDto((Employee) employee);
                employeeDTO.employeeAssignment = employeeAssignmentService.findMainByUser(employee.user).orElse(null);
                return employeeDTO;
            });
    }

    public Long countAllByClient(String clientId) {
        log.debug("Request to count all Employees by Client");
        return employeeRepository.countAllByClient(clientId);
    }

    public Long countAllByAssignment(String assignmentId) {
        log.debug("Request to count all Employees by Assignment");
        return employeeRepository.countAllByAssignment(assignmentId);
    }

    public Long countAllByPosition(String positionId) {
        log.debug("Request to count all Employees by Position");
        return employeeRepository.countAllByPosition(positionId);
    }


}
