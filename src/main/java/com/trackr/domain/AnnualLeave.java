package com.trackr.domain;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.trackr.domain.enumeration.AnnualLeaveStatus;
import org.bson.types.ObjectId;

/**
 * A AnnualLeave.
 */
@MongoEntity(collection="annual_leave")
@RegisterForReflection
public class AnnualLeave extends PanacheMongoEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @BsonId
    public ObjectId id;

    @NotNull
    public String user;

    @NotNull
    public Instant fromDate;

    @NotNull
    public Instant toDate;

    @NotNull
    @Size(min = 2)
    public String description;

    public String remarks;

    @NotNull
    public AnnualLeaveStatus status;

    @NotNull
    public String organisation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnualLeave)) {
            return false;
        }
        return id != null && id.equals(((AnnualLeave) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AnnualLeave{" +
            "id=" + id +
            ", user='" + user + "'" +
            ", fromDate='" + fromDate + "'" +
            ", toDate='" + toDate + "'" +
            ", description='" + description + "'" +
            ", remarks='" + remarks + "'" +
            ", status='" + status + "'" +
            ", organisation='" + organisation + "'" +
            "}";
    }
}
