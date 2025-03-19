package com.trackr.domain;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Organisation.
 */
@MongoEntity(collection="organisation")
@RegisterForReflection
public class Organisation extends PanacheMongoEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @BsonId
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

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organisation)) {
            return false;
        }
        return id != null && id.equals(((Organisation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Organisation{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", sinceDate='" + sinceDate + "'" +
            ", joinCode='" + joinCode + "'" +
            ", companyReg='" + companyReg + "'" +
            ", address='" + address + "'" +
            "}";
    }
}
