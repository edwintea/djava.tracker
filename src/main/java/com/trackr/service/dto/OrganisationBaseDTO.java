package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@RegisterForReflection
public class OrganisationBaseDTO implements Serializable {

    public OrganisationDTO organisation;
    public List<EmployeeDTO> employees;
    public List<DepartmentDTO> departments;
    public List<PositionEmployeeCountDTO> positions;
    public List<ClientEmployeeCountDTO> clients;
    public List<AssignmentEmployeeCountDTO> assignments;
    public List<NewsletterDTO> newsletters;

    public OrganisationBaseDTO() {

    }

    public OrganisationBaseDTO(OrganisationDTO organisation, List<EmployeeDTO> employees, List<DepartmentDTO> departments, List<PositionEmployeeCountDTO> positions, List<ClientEmployeeCountDTO> clients, List<AssignmentEmployeeCountDTO> assignments, List<NewsletterDTO> newsletters) {
        this.organisation = organisation;
        this.employees = employees;
        this.departments = departments;
        this.positions = positions;
        this.clients = clients;
        this.assignments = assignments;
        this.newsletters = newsletters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganisationBaseDTO that = (OrganisationBaseDTO) o;

        if (!Objects.equals(organisation, that.organisation)) return false;
        if (!Objects.equals(employees, that.employees)) return false;
        if (!Objects.equals(departments, that.departments)) return false;
        if (!Objects.equals(positions, that.positions)) return false;
        if (!Objects.equals(clients, that.clients)) return false;
        if (!Objects.equals(assignments, that.assignments)) return false;
        return Objects.equals(newsletters, that.newsletters);
    }

    @Override
    public int hashCode() {
        int result = organisation != null ? organisation.hashCode() : 0;
        result = 31 * result + (employees != null ? employees.hashCode() : 0);
        result = 31 * result + (departments != null ? departments.hashCode() : 0);
        result = 31 * result + (positions != null ? positions.hashCode() : 0);
        result = 31 * result + (clients != null ? clients.hashCode() : 0);
        result = 31 * result + (assignments != null ? assignments.hashCode() : 0);
        result = 31 * result + (newsletters != null ? newsletters.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrganisationBaseDTO{" +
            "organisation=" + organisation +
            ", employees=" + employees +
            ", departments=" + departments +
            ", positions=" + positions +
            ", clients=" + clients +
            ", assignments=" + assignments +
            ", newsletters=" + newsletters +
            '}';
    }
}
