package com.trackr.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import org.bson.types.ObjectId;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.trackr.domain.Newsletter} entity.
 */
@RegisterForReflection
public class NewsletterDTO implements Serializable {

    public ObjectId id;

    @NotNull
    @Size(min = 2)
    public String subject;

    @NotNull
    public String body;

    @NotNull
    public Instant createdDate;

    @NotNull
    public String organisation;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NewsletterDTO)) {
            return false;
        }

        return id != null && id.equals(((NewsletterDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "NewsletterDTO{" +
            "id=" + id +
            ", subject='" + subject + "'" +
            ", body='" + body + "'" +
            ", createdDate='" + createdDate + "'" +
            ", organisation='" + organisation + "'" +
            "}";
    }
}
