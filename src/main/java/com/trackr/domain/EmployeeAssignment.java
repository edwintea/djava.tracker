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
 * A EmployeeAssignment.
 */
@MongoEntity(collection="employee_assignment")
@RegisterForReflection
public class EmployeeAssignment extends PanacheMongoEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @BsonId
    public ObjectId id;

    @NotNull
    public Boolean active;

    @NotNull
    public Boolean main;

    @NotNull
    public String client;

    @NotNull
    public Instant startDate;

    public Instant toDate;

    @NotNull
    public AssignmentType assignmentType;

    public String description;

    @NotNull
    public String clientName;

    @NotNull
    public String assignment;

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
        if (!(o instanceof EmployeeAssignment)) {
            return false;
        }
        return id != null && id.equals(((EmployeeAssignment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EmployeeAssignment{" +
            "id=" + id +
            ", active='" + active + "'" +
            ", main='" + main + "'" +
            ", client='" + client + "'" +
            ", startDate='" + startDate + "'" +
            ", toDate='" + toDate + "'" +
            ", type='" + assignmentType + "'" +
            ", description='" + description + "'" +
            ", clientName='" + clientName + "'" +
            ", assignment='" + assignment + "'" +
            ", user='" + user + "'" +
            ", organisation='" + organisation + "'" +
            "}";
    }
}
