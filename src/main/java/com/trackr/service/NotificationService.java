package com.trackr.service;

import io.quarkus.panache.common.Page;
import com.trackr.domain.Notification;
import com.trackr.repository.NotificationRepository;
import com.trackr.service.dto.NotificationDTO;
import com.trackr.service.mapper.NotificationMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Inject
    NotificationRepository notificationRepository;

    @Inject
    NotificationMapper notificationMapper;

    public NotificationDTO persistOrUpdate(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        var notification = notificationMapper.toEntity(notificationDTO);
        notificationRepository.persistOrUpdate(notification);
        return notificationMapper.toDto(notification);
    }

    /**
     * Delete the Notification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.findByIdOptional(new ObjectId(id)).ifPresent(notification -> {
            notificationRepository.delete(notification);
        });
    }

    /**
     * Get one notification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<NotificationDTO> findOne(String id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findByIdOptional(new ObjectId(id))
            .map(notification -> notificationMapper.toDto((Notification) notification));
    }

    /**
     * Get all the notifications.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<NotificationDTO> findAll(Page page) {
        log.debug("Request to get all Notifications");
        return new Paged<>(notificationRepository.findAll().page(page))
            .map(notification -> notificationMapper.toDto((Notification) notification));
    }



}
