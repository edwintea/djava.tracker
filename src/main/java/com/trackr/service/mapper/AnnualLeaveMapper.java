package com.trackr.service.mapper;


import com.trackr.domain.*;
import com.trackr.service.dto.AnnualLeaveDTO;

import org.bson.types.ObjectId;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AnnualLeave} and its DTO {@link AnnualLeaveDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface AnnualLeaveMapper extends EntityMapper<AnnualLeaveDTO, AnnualLeave> {



    default AnnualLeave fromId(ObjectId id) {
        if (id == null) {
            return null;
        }
        AnnualLeave annualLeave = new AnnualLeave();
        annualLeave.id = id;
        return annualLeave;
    }
}
