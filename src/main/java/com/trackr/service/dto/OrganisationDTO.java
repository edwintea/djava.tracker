package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.trackr.domain.Organisation} entity.
 */
@RegisterForReflection
public class OrganisationDTO implements Serializable {

    public ObjectId id;

    @NotNull
    @Size(min = 2)
    public String name;

    @NotNull
    public Instant sinceDate;

    @NotNull
    public String joinCode;

    @NotNull
    public String companyReg;

    public String address;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganisationDTO)) {
            return false;
        }

        return id != null && id.equals(((OrganisationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OrganisationDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", sinceDate='" + sinceDate + "'" +
            ", joinCode='" + joinCode + "'" +
            ", companyReg='" + companyReg + "'" +
            ", address='" + address + "'" +
            "}";
    }
}
