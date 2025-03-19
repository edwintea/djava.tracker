package com.trackr.service.mapper;


import com.trackr.domain.*;
import com.trackr.service.dto.NotificationDTO;

import org.bson.types.ObjectId;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {



    default Notification fromId(ObjectId id) {
        if (id == null) {
            return null;
        }
        Notification notification = new Notification();
        notification.id = id;
        return notification;
    }
}
