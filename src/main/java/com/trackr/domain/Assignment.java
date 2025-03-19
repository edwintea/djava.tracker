package com.trackr.domain;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.trackr.domain.enumeration.AssignmentType;
import org.bson.types.ObjectId;

/**
 * A Assignment.
 */
@MongoEntity(collection="assignment")
@RegisterForReflection
public class Assignment extends PanacheMongoEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @BsonId
    public ObjectId id;

    public String clientName;

    @NotNull
    public String client;

    @NotNull
    public AssignmentType assignmentType;

    public String description;

    @NotNull
    public String organisation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assignment)) {
            return false;
        }
        return id != null && id.equals(((Assignment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Assignment{" +
            "id=" + id +
            ", clientName='" + clientName + "'" +
            ", client='" + client + "'" +
            ", type='" + assignmentType + "'" +
            ", description='" + description + "'" +
            ", organisation='" + organisation + "'" +
            "}";
    }
}
