package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.trackr.domain.Department} entity.
 */
@RegisterForReflection
public class DepartmentDTO implements Serializable {

    public ObjectId id;

    @NotNull
    @Size(min = 2)
    public String name;

    @NotNull
    public String organisation;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartmentDTO)) {
            return false;
        }

        return id != null && id.equals(((DepartmentDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DepartmentDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", organisation='" + organisation + "'" +
            "}";
    }
}
