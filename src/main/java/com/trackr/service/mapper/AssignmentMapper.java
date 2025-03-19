package com.trackr.service.mapper;


import com.trackr.domain.*;
import com.trackr.service.dto.AssignmentDTO;

import org.bson.types.ObjectId;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Assignment} and its DTO {@link AssignmentDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface AssignmentMapper extends EntityMapper<AssignmentDTO, Assignment> {



    default Assignment fromId(ObjectId id) {
        if (id == null) {
            return null;
        }
        Assignment assignment = new Assignment();
        assignment.id = id;
        return assignment;
    }
}
