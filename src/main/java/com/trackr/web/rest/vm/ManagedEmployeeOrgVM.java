package com.trackr.web.rest.vm;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.validation.constraints.NotNull;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
@RegisterForReflection
public class ManagedEmployeeOrgVM extends ManagedUserVM {

    public ManagedEmployeeOrgVM() {
        // Empty constructor needed for Jackson.
    }

    @NotNull
    public String organisationName;

    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
