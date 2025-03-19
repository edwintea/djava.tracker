package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;

/**
 * A DTO for the {@link com.trackr.domain.Client} entity.
 */
@RegisterForReflection
public class ClientEmployeeCountDTO extends ClientDTO implements Serializable {

    public Long employeeCount;

    public ClientEmployeeCountDTO(ClientDTO client, Long employeeCount) {
        super.id = client.id;
        super.name = client.name;
        super.address = client.address;
        super.sinceDate = client.sinceDate;
        super.organisation = client.organisation;
        this.employeeCount = employeeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientEmployeeCountDTO)) {
            return false;
        }

        return id != null && id.equals(((ClientEmployeeCountDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", employeeCount='" + employeeCount + "'" +
            ", sinceDate='" + sinceDate + "'" +
            ", address='" + address + "'" +
            ", organisation='" + organisation + "'" +
            "}";
    }
}
