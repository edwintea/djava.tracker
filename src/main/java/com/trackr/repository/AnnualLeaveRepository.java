package com.trackr.repository;

import com.trackr.domain.AnnualLeave;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import javax.enterprise.context.ApplicationScoped;

/**
 * Hibernate PanacheMongoDB repository for the AnnualLeave entity.
 */
@SuppressWarnings("unused")
@ApplicationScoped
public class AnnualLeaveRepository implements PanacheMongoRepository<AnnualLeave> {


}
