package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.trackr.domain.enumeration.AnnualLeaveStatus;
import org.bson.types.ObjectId;

/**
 * A DTO for the {@link com.trackr.domain.AnnualLeave} entity.
 */
@RegisterForReflection
public class AnnualLeaveDTO implements Serializable {

    public ObjectId id;

    @NotNull
    public String user;

    @NotNull
    public Instant fromDate;

    @NotNull
    public Instant toDate;

    @NotNull
    @Size(min = 2)
    public String description;

    public String remarks;

    @NotNull
    public AnnualLeaveStatus status;

    @NotNull
    public String organisation;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnualLeaveDTO)) {
            return false;
        }

        return id != null && id.equals(((AnnualLeaveDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AnnualLeaveDTO{" +
            "id=" + id +
            ", user='" + user + "'" +
            ", fromDate='" + fromDate + "'" +
            ", toDate='" + toDate + "'" +
            ", description='" + description + "'" +
            ", remarks='" + remarks + "'" +
            ", status='" + status + "'" +
            ", organisation='" + organisation + "'" +
            "}";
    }
}
