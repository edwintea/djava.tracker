package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.trackr.domain.Position} entity.
 */
@RegisterForReflection
public class PositionDTO implements Serializable {

    public ObjectId id;

    @NotNull
    @Size(min = 2)
    public String name;

    public String departmentName;

    @NotNull
    public String department;

    @NotNull
    public String organisation;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionDTO)) {
            return false;
        }

        return id != null && id.equals(((PositionDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PositionDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", departmentName='" + departmentName + "'" +
            ", department='" + department + "'" +
            ", organisation='" + organisation + "'" +
            "}";
    }
}
