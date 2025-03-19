package com.trackr.service.mapper;


import com.trackr.domain.*;
import com.trackr.service.dto.NewsletterDTO;

import org.bson.types.ObjectId;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Newsletter} and its DTO {@link NewsletterDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface NewsletterMapper extends EntityMapper<NewsletterDTO, Newsletter> {



    default Newsletter fromId(ObjectId id) {
        if (id == null) {
            return null;
        }
        Newsletter newsletter = new Newsletter();
        newsletter.id = id;
        return newsletter;
    }
}
