package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link com.trackr.domain.Position} entity.
 */
@RegisterForReflection
public class PositionEmployeeCountDTO extends PositionDTO implements Serializable {

    public Long employeeCount;

    public PositionEmployeeCountDTO(PositionDTO position, Long employeeCount) {
        super.id = position.id;
        super.department = position.department;
        super.departmentName = position.departmentName;
        super.name = position.name;
        super.organisation = position.organisation;
        this.employeeCount = employeeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PositionEmployeeCountDTO)) {
            return false;
        }

        return id != null && id.equals(((PositionEmployeeCountDTO) o).id);
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
