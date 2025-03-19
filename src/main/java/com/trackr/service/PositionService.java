package com.trackr.service;

import com.trackr.service.dto.*;
import io.quarkus.panache.common.Page;
import com.trackr.domain.Position;
import com.trackr.repository.PositionRepository;
import com.trackr.service.mapper.PositionMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class PositionService {

    private final Logger log = LoggerFactory.getLogger(PositionService.class);

    @Inject
    PositionRepository positionRepository;

    @Inject
    PositionMapper positionMapper;

    @Inject
    DepartmentService departmentService;

    @Inject
    EmployeeService employeeService;

    public PositionDTO persistOrUpdate(PositionDTO positionDTO) {
        log.debug("Request to save Position : {}", positionDTO);
        if(positionDTO.departmentName == null || positionDTO.departmentName.isEmpty()) {
            DepartmentDTO department = departmentService.findOne(positionDTO.department).orElseThrow();
            positionDTO.departmentName = department.name;
        }
        var position = positionMapper.toEntity(positionDTO);
        positionRepository.persistOrUpdate(position);
        return positionMapper.toDto(position);
    }

    /**
     * Delete the Position by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Position : {}", id);
        positionRepository.findByIdOptional(new ObjectId(id)).ifPresent(position -> {
            positionRepository.delete(position);
        });
    }

    /**
     * Get one position by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<PositionDTO> findOne(String id) {
        log.debug("Request to get Position : {}", id);
        return positionRepository.findByIdOptional(new ObjectId(id))
            .map(position -> positionMapper.toDto((Position) position));
    }

    /**
     * Get all the positions.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<PositionDTO> findAll(Page page) {
        log.debug("Request to get all Positions");
        return new Paged<>(positionRepository.findAll().page(page))
            .map(position -> positionMapper.toDto((Position) position));
    }

    public List<PositionEmployeeCountDTO> findAllByPosition(String organisationId) {
        log.debug("Request to get all Position");
        return positionRepository.findPositionByOrganisation(organisationId)
            .stream().map(position -> new PositionEmployeeCountDTO(positionMapper.toDto(position), employeeService.countAllByPosition(position.id.toString()))).collect(Collectors.toList());
    }



}
