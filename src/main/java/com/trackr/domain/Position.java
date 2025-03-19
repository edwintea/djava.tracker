package com.trackr.domain;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Position.
 */
@MongoEntity(collection="position")
@RegisterForReflection
public class Position extends PanacheMongoEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @BsonId
    public ObjectId id;

    @NotNull
    @Size(min = 2)
    public String name;

    public String departmentName;

    @NotNull
    public String department;

    @NotNull
    public String organisation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        return id != null && id.equals(((Position) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Position{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", departmentName='" + departmentName + "'" +
            ", department='" + department + "'" +
            ", organisation='" + organisation + "'" +
            "}";
    }
}
