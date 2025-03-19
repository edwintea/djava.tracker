package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

/**
 * A DTO for the {@link com.trackr.domain.Client} entity.
 */
@RegisterForReflection
public class AssignmentEmployeeCountDTO extends AssignmentDTO implements Serializable {

    public Long employeeCount;

    public AssignmentEmployeeCountDTO(AssignmentDTO assignment, Long employeeCount) {
        super.id = assignment.id;
        super.client = assignment.client;
        super.clientName = assignment.clientName;
        super.assignmentType = assignment.assignmentType;
        super.description = assignment.description;
        super.organisation = assignment.organisation;
        this.employeeCount = employeeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssignmentEmployeeCountDTO)) {
            return false;
        }

        return id != null && id.equals(((AssignmentEmployeeCountDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AssignmentEmployeeCountDTO{" +
            "id=" + id +
            ", clientName='" + clientName + '\'' +
            ", client='" + client + '\'' +
            ", assignmentType=" + assignmentType +
            ", description='" + description + '\'' +
            ", organisation='" + organisation + '\'' +
            ", employeeCount=" + employeeCount +
            '}';
    }
}
