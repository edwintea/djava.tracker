package com.trackr.service.mapper;


import com.trackr.domain.*;
import com.trackr.service.dto.ClientDTO;

import org.bson.types.ObjectId;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {



    default Client fromId(ObjectId id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.id = id;
        return client;
    }
}
