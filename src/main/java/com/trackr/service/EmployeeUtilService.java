package com.trackr.service;

import com.trackr.domain.enumeration.EmployeeType;
import com.trackr.repository.AssignmentRepository;
import com.trackr.service.dto.*;
import com.trackr.service.mapper.AssignmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@ApplicationScoped
public class EmployeeUtilService {

    private final Logger log = LoggerFactory.getLogger(EmployeeUtilService.class);

    @Inject
    AssignmentService assignmentService;

    @Inject
    ClientService clientService;

    @Inject
    PositionService positionService;

    @Inject
    DepartmentService departmentService;

    @Inject
    EmployeeService employeeService;

    public EmployeeDTO populateUserFields(EmployeeDTO employee, String fullName, String userId) {
        employee.fullName = fullName;
        employee.user = userId;

        return employee;
    }

    public EmployeeDTO populateHRFields(EmployeeDTO employee, Boolean privileged, String organisationId) {
        employee.isAssignable = !privileged;
        employee.sinceDate = null;
        employee.type = EmployeeType.PERMANENT;
        employee.reportsToName = null;
        employee.positionName = null;
        employee.departmentName = null;
        employee.reportsTo = null;
        employee.position = null;
        employee.department = null;
        employee.organisation = organisationId;

        return employee;
    }

}
