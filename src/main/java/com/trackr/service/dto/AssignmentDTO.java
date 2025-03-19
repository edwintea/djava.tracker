package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.trackr.domain.enumeration.AssignmentType;
import org.bson.types.ObjectId;

/**
 * A DTO for the {@link com.trackr.domain.Assignment} entity.
 */
@RegisterForReflection
public class AssignmentDTO implements Serializable {

    public ObjectId id;

    public String clientName;

    @NotNull
    public String client;

    @NotNull
    public AssignmentType assignmentType;

    public String description;

    @NotNull
    public String organisation;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssignmentDTO)) {
            return false;
        }

        return id != null && id.equals(((AssignmentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AssignmentDTO{" +
            "id=" + id +
            ", clientName='" + clientName + "'" +
            ", client='" + client + "'" +
            ", type='" + assignmentType + "'" +
            ", description='" + description + "'" +
            ", organisation='" + organisation + "'" +
            "}";
    }
}
