package com.trackr.service.dto;


import com.trackr.domain.enumeration.ClientType;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.trackr.domain.Client} entity.
 */
@RegisterForReflection
public class ClientMinimalDTO implements Serializable {

    public ObjectId id;

    @NotNull
    public String name;

    @NotNull
    public ClientType type;

    public ClientMinimalDTO(ClientDTO client) {
        this.id = client.id;
        this.name = client.name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientMinimalDTO)) {
            return false;
        }

        return id != null && id.equals(((ClientMinimalDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ClientMinimalDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", type=" + type +
            '}';
    }

}
