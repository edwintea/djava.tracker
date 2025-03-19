package com.trackr.service;

import io.quarkus.panache.common.Page;
import com.trackr.domain.Department;
import com.trackr.repository.DepartmentRepository;
import com.trackr.service.dto.DepartmentDTO;
import com.trackr.service.mapper.DepartmentMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class DepartmentService {

    private final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    @Inject
    DepartmentRepository departmentRepository;

    @Inject
    DepartmentMapper departmentMapper;

    public DepartmentDTO persistOrUpdate(DepartmentDTO departmentDTO) {
        log.debug("Request to save Department : {}", departmentDTO);
        var department = departmentMapper.toEntity(departmentDTO);
        departmentRepository.persistOrUpdate(department);
        return departmentMapper.toDto(department);
    }

    /**
     * Delete the Department by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Department : {}", id);
        departmentRepository.findByIdOptional(new ObjectId(id)).ifPresent(department -> {
            departmentRepository.delete(department);
        });
    }

    /**
     * Get one department by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<DepartmentDTO> findOne(String id) {
        log.debug("Request to get Department : {}", id);
        return departmentRepository.findByIdOptional(new ObjectId(id))
            .map(department -> departmentMapper.toDto((Department) department));
    }

    /**
     * Get all the departments.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<DepartmentDTO> findAll(Page page) {
        log.debug("Request to get all Departments");
        return new Paged<>(departmentRepository.findAll().page(page))
            .map(department -> departmentMapper.toDto((Department) department));
    }

    public List<DepartmentDTO> findAllByOrganisation(String organisationId) {
        log.debug("Request to get all Departments by Organisation");
        return departmentRepository.findAllByOrganisation(organisationId)
            .stream().map(department -> departmentMapper.toDto(department)).collect(Collectors.toList());
    }



}
