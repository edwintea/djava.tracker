package com.trackr.domain;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.trackr.domain.enumeration.NotificationType;
import org.bson.types.ObjectId;

/**
 * A Notification.
 */
@MongoEntity(collection="notification")
@RegisterForReflection
public class Notification extends PanacheMongoEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @BsonId
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

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Notification{" +
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
