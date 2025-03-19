package com.trackr.service.mapper;


import com.trackr.domain.*;
import com.trackr.service.dto.DepartmentDTO;

import org.bson.types.ObjectId;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {



    default Department fromId(ObjectId id) {
        if (id == null) {
            return null;
        }
        Department department = new Department();
        department.id = id;
        return department;
    }
}
