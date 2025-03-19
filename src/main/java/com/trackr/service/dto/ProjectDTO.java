package com.trackr.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.Instant;

@RegisterForReflection
public class ProjectDTO {

    public String clientName;

    public String status;

    public Integer employees;

    public Instant targetCompletion;

    public ProjectDTO(String clientName, String status, Integer employees, Instant targetCompletion) {
        this.clientName = clientName;
        this.status = status;
        this.employees = employees;
        this.targetCompletion = targetCompletion;
    }

    @Override
    public int hashCode() {
        int result = clientName != null ? clientName.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (employees != null ? employees.hashCode() : 0);
        result = 31 * result + (targetCompletion != null ? targetCompletion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
            "clientName='" + clientName + '\'' +
            ", status='" + status + '\'' +
            ", employees=" + employees +
            ", targetCompletion=" + targetCompletion +
            '}';
    }
}
