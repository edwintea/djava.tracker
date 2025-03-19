package com.trackr.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;
import java.util.Objects;

@RegisterForReflection
public class DashboardComboDTO {

    public List<ClientEmployeeCountDTO> clients;

    public EmployeeOverviewDTO employeeOverview;

    public ClientOverviewDTO clientOverview;

    public List<ProjectDTO> projects;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DashboardComboDTO that = (DashboardComboDTO) o;

        if (!Objects.equals(clients, that.clients)) return false;
        if (!Objects.equals(employeeOverview, that.employeeOverview))
            return false;
        if (!Objects.equals(clientOverview, that.clientOverview))
            return false;
        return Objects.equals(projects, that.projects);
    }

    @Override
    public int hashCode() {
        int result = clients != null ? clients.hashCode() : 0;
        result = 31 * result + (employeeOverview != null ? employeeOverview.hashCode() : 0);
        result = 31 * result + (clientOverview != null ? clientOverview.hashCode() : 0);
        result = 31 * result + (projects != null ? projects.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DashboardComboDTO{" +
            "clients=" + clients +
            ", employeeOverview=" + employeeOverview +
            ", clientOverview=" + clientOverview +
            ", projects=" + projects +
            '}';
    }
}
