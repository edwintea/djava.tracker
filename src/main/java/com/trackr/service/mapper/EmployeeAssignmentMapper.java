package com.trackr.service.mapper;


import com.trackr.domain.*;
import com.trackr.service.dto.EmployeeAssignmentDTO;

import org.bson.types.ObjectId;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmployeeAssignment} and its DTO {@link EmployeeAssignmentDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface EmployeeAssignmentMapper extends EntityMapper<EmployeeAssignmentDTO, EmployeeAssignment> {



    default EmployeeAssignment fromId(ObjectId id) {
        if (id == null) {
            return null;
        }
        EmployeeAssignment employeeAssignment = new EmployeeAssignment();
        employeeAssignment.id = id;
        return employeeAssignment;
    }
}
