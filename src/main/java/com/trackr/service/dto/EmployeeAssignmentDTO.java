package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.trackr.domain.enumeration.AssignmentType;
import org.bson.types.ObjectId;

/**
 * A DTO for the {@link com.trackr.domain.EmployeeAssignment} entity.
 */
@RegisterForReflection
public class EmployeeAssignmentDTO implements Serializable {

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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeAssignmentDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployeeAssignmentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EmployeeAssignmentDTO{" +
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
