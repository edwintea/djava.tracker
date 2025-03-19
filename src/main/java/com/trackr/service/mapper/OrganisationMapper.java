package com.trackr.service.mapper;


import com.trackr.domain.*;
import com.trackr.service.dto.OrganisationDTO;

import org.bson.types.ObjectId;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Organisation} and its DTO {@link OrganisationDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface OrganisationMapper extends EntityMapper<OrganisationDTO, Organisation> {



    default Organisation fromId(ObjectId id) {
        if (id == null) {
            return null;
        }
        Organisation organisation = new Organisation();
        organisation.id = id;
        return organisation;
    }
}
