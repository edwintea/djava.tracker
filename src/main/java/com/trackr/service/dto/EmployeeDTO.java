package com.trackr.service.dto;


import com.trackr.domain.enumeration.EmployeeType;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.trackr.domain.Employee} entity.
 */
@RegisterForReflection
public class EmployeeDTO implements Serializable {

    public ObjectId id;

    @NotNull
    public String fullName;

    public Instant sinceDate;

    @NotNull
    public EmployeeType type;

    public String positionName;

    public String departmentName;

    public String reportsToName;

    public EmployeeAssignmentDTO employeeAssignment;

    @NotNull
    public Boolean isAssignable;

    @NotNull
    public Boolean isAssigned;

    public String department;

    public String position;

    @NotNull
    public String user;

    public String reportsTo;

    @NotNull
    public String organisation;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployeeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id=" + id +
            ", fullName='" + fullName + '\'' +
            ", sinceDate=" + sinceDate +
            ", type=" + type +
            ", positionName='" + positionName + '\'' +
            ", departmentName='" + departmentName + '\'' +
            ", reportsToName='" + reportsToName + '\'' +
            ", employeeAssignment='" + employeeAssignment + '\'' +
            ", isAssignable=" + isAssignable +
            ", isAssigned=" + isAssigned +
            ", department='" + department + '\'' +
            ", position='" + position + '\'' +
            ", user='" + user + '\'' +
            ", reportsTo='" + reportsTo + '\'' +
            ", organisation='" + organisation + '\'' +
            '}';
    }
}
