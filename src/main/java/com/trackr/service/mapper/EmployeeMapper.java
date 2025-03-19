package com.trackr.service.mapper;


import com.trackr.domain.*;
import com.trackr.service.dto.EmployeeDTO;

import org.bson.types.ObjectId;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {



    default Employee fromId(ObjectId id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.id = id;
        return employee;
    }
}
