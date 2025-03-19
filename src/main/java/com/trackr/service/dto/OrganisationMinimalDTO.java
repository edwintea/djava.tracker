package com.trackr.service.dto;


import com.trackr.domain.Organisation;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.trackr.domain.Organisation} entity.
 */
@RegisterForReflection
public class OrganisationMinimalDTO implements Serializable {

    @NotNull
    @Size(min = 2)
    public String name;

    @NotNull
    public String joinCode;

    public OrganisationMinimalDTO(OrganisationDTO organisationDTO) {
        this.name = organisationDTO.name;
        this.joinCode = organisationDTO.joinCode;
    }

    public OrganisationMinimalDTO(Organisation organisation) {
        this.name = organisation.name;
        this.joinCode = organisation.joinCode;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (joinCode != null ? joinCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrganisationDTO{" +
            "name='" + name + "'" +
            ", joinCode='" + joinCode + "'" +
            "}";
    }
}
