package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

@RegisterForReflection
public class AuthenticationDTO implements Serializable {

    public String idToken;

    public UserDTO user;

    public EmployeeDTO employee;

    public AuthenticationDTO (String idToken, UserDTO user, EmployeeDTO employee) {
        this.idToken = idToken;
        this.user = user;
        this.employee = employee;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AuthenticationDTO{" +
            "idToken='" + idToken + '\'' +
            ", user=" + user.toString() +
            ", employee=" + employee.toString() +
            '}';
    }
}
