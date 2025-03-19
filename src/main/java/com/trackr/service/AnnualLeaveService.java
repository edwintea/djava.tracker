package com.trackr.service;

import io.quarkus.panache.common.Page;
import com.trackr.domain.AnnualLeave;
import com.trackr.repository.AnnualLeaveRepository;
import com.trackr.service.dto.AnnualLeaveDTO;
import com.trackr.service.mapper.AnnualLeaveMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AnnualLeaveService {

    private final Logger log = LoggerFactory.getLogger(AnnualLeaveService.class);

    @Inject
    AnnualLeaveRepository annualLeaveRepository;

    @Inject
    AnnualLeaveMapper annualLeaveMapper;

    public AnnualLeaveDTO persistOrUpdate(AnnualLeaveDTO annualLeaveDTO) {
        log.debug("Request to save AnnualLeave : {}", annualLeaveDTO);
        var annualLeave = annualLeaveMapper.toEntity(annualLeaveDTO);
        annualLeaveRepository.persistOrUpdate(annualLeave);
        return annualLeaveMapper.toDto(annualLeave);
    }

    /**
     * Delete the AnnualLeave by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete AnnualLeave : {}", id);
        annualLeaveRepository.findByIdOptional(new ObjectId(id)).ifPresent(annualLeave -> {
            annualLeaveRepository.delete(annualLeave);
        });
    }

    /**
     * Get one annualLeave by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<AnnualLeaveDTO> findOne(String id) {
        log.debug("Request to get AnnualLeave : {}", id);
        return annualLeaveRepository.findByIdOptional(new ObjectId(id))
            .map(annualLeave -> annualLeaveMapper.toDto((AnnualLeave) annualLeave));
    }

    /**
     * Get all the annualLeaves.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<AnnualLeaveDTO> findAll(Page page) {
        log.debug("Request to get all AnnualLeaves");
        return new Paged<>(annualLeaveRepository.findAll().page(page))
            .map(annualLeave -> annualLeaveMapper.toDto((AnnualLeave) annualLeave));
    }



}
