package com.trackr.domain;
import javax.json.bind.annotation.JsonbTransient;

import com.trackr.domain.enumeration.AssignmentType;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.trackr.domain.enumeration.EmployeeType;
import org.bson.types.ObjectId;

/**
 * A Employee.
 */
@MongoEntity(collection="employee")
@RegisterForReflection
public class Employee extends PanacheMongoEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @BsonId
    public ObjectId id;

    @NotNull
    public String fullName;

    public Instant sinceDate;

    @NotNull
    public EmployeeType type;

    public String positionName;

    public String departmentName;

    public String reportsToName;

    @NotNull
    public Boolean isAssignable;

    @NotNull
    public Boolean isAssigned;

    public String department;

    public String position;

    @NotNull
    public String user;

    public String reportsTo;

    @NotNull
    public String organisation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + id +
            ", fullName='" + fullName + '\'' +
            ", sinceDate=" + sinceDate +
            ", type=" + type +
            ", positionName='" + positionName + '\'' +
            ", departmentName='" + departmentName + '\'' +
            ", reportsToName='" + reportsToName + '\'' +
            ", isAssignable=" + isAssignable +
            ", isAssigned=" + isAssigned +
            ", department='" + department + '\'' +
            ", position='" + position + '\'' +
            ", user='" + user + '\'' +
            ", reportsTo='" + reportsTo + '\'' +
            ", organisation='" + organisation + '\'' +
            '}';
    }
}
