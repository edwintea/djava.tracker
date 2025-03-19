package com.trackr.service.mapper;


import com.trackr.domain.*;
import com.trackr.service.dto.PositionDTO;

import org.bson.types.ObjectId;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Position} and its DTO {@link PositionDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface PositionMapper extends EntityMapper<PositionDTO, Position> {



    default Position fromId(ObjectId id) {
        if (id == null) {
            return null;
        }
        Position position = new Position();
        position.id = id;
        return position;
    }
}
