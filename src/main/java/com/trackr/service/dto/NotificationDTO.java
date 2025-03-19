package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.trackr.domain.enumeration.NotificationType;
import org.bson.types.ObjectId;

/**
 * A DTO for the {@link com.trackr.domain.Notification} entity.
 */
@RegisterForReflection
public class NotificationDTO implements Serializable {

    public ObjectId id;

    @NotNull
    public NotificationType type;

    @NotNull
    @Size(min = 2)
    public String subject;

    @NotNull
    public String body;

    @NotNull
    public Instant createdDate;

    @NotNull
    public String user;

    @NotNull
    public String organisation;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        return id != null && id.equals(((NotificationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", subject='" + subject + "'" +
            ", body='" + body + "'" +
            ", createdDate='" + createdDate + "'" +
            ", user='" + user + "'" +
            ", organisation='" + organisation + "'" +
            "}";
    }
}
