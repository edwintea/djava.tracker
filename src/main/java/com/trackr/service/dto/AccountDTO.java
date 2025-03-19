package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

@RegisterForReflection
public class AccountDTO implements Serializable {

    public UserDTO user;

    public EmployeeDTO employee;

    public AccountDTO(UserDTO user, EmployeeDTO employee) {
        this.user = user;
        this.employee = employee;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (employee != null ? employee.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
            "user=" + user +
            ", employee=" + employee +
            '}';
    }
}
