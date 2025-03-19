package com.trackr.repository;

import com.trackr.domain.Notification;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import javax.enterprise.context.ApplicationScoped;

/**
 * Hibernate PanacheMongoDB repository for the Notification entity.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class NotificationRepository implements PanacheMongoRepository<Notification> {


}
