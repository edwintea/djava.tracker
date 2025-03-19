package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import com.trackr.domain.enumeration.ClientType;
import org.bson.types.ObjectId;

/**
 * A DTO for the {@link com.trackr.domain.Client} entity.
 */
@RegisterForReflection
public class ClientDTO implements Serializable {

    public ObjectId id;

    @NotNull
    public String name;

    @NotNull
    public Instant sinceDate;

    public String address;

    @NotNull
    public String organisation;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientDTO)) {
            return false;
        }

        return id != null && id.equals(((ClientDTO) o).id);
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
            ", sinceDate='" + sinceDate + "'" +
            ", address='" + address + "'" +
            ", organisation='" + organisation + "'" +
            "}";
    }
}
