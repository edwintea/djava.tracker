package com.trackr.domain;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.trackr.domain.enumeration.ClientType;
import org.bson.types.ObjectId;

/**
 * A Client.
 */
@MongoEntity(collection="client")
@RegisterForReflection
public class Client extends PanacheMongoEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @BsonId
    public ObjectId id;

    @NotNull
    public String name;

    @NotNull
    public Instant sinceDate;

    public String address;

    @NotNull
    public String organisation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", sinceDate='" + sinceDate + "'" +
            ", address='" + address + "'" +
            ", organisation='" + organisation + "'" +
            "}";
    }
}
