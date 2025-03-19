package com.trackr.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class EmployeeOverviewDTO {

    public Integer totalEmployees;

    public Integer totalOnLeave;

    public EmployeeOverviewDTO(Integer totalEmployees, Integer totalOnLeave) {
        this.totalEmployees = totalEmployees;
        this.totalOnLeave = totalOnLeave;
    }

    @Override
    public int hashCode() {
        int result = totalEmployees != null ? totalEmployees.hashCode() : 0;
        result = 31 * result + (totalOnLeave != null ? totalOnLeave.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EmployeeOverviewDTO{" +
            "totalEmployees=" + totalEmployees +
            ", totalOnLeave=" + totalOnLeave +
            '}';
    }
}
