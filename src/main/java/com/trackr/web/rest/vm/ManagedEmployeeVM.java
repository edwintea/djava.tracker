package com.trackr.web.rest.vm;

import com.trackr.service.dto.UserDTO;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
@RegisterForReflection
public class ManagedEmployeeVM extends ManagedUserVM {

    public ManagedEmployeeVM() {
        // Empty constructor needed for Jackson.
    }

    @NotNull
    public String organisationCode;

    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
