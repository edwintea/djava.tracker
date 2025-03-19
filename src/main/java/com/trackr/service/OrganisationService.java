package com.trackr.service;

import com.trackr.domain.User;
import com.trackr.domain.enumeration.AssignmentType;
import com.trackr.security.RandomUtil;
import com.trackr.service.dto.*;
import io.quarkus.panache.common.Page;
import com.trackr.domain.Organisation;
import com.trackr.repository.OrganisationRepository;
import com.trackr.service.mapper.OrganisationMapper;
import io.quarkus.security.identity.SecurityIdentity;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrganisationService {

    private final Logger log = LoggerFactory.getLogger(OrganisationService.class);

    @Inject
    OrganisationRepository organisationRepository;

    @Inject
    OrganisationMapper organisationMapper;

    @Inject
    UserService userService;

    @Inject
    EmployeeService employeeService;

    @Inject
    ClientService clientService;

    @Inject
    DepartmentService departmentService;

    @Inject
    PositionService positionService;

    @Inject
    AssignmentService assignmentService;

    @Inject
    NewsletterService newsletterService;

    @Inject
    EmployeeAssignmentService employeeAssignmentService;

    @Inject
    SecurityIdentity securityIdentity;

    public OrganisationDTO persistOrUpdate(OrganisationDTO organisationDTO) {
        log.debug("Request to save Organisation : {}", organisationDTO);
        var organisation = organisationMapper.toEntity(organisationDTO);
        organisationRepository.persistOrUpdate(organisation);
        return organisationMapper.toDto(organisation);
    }

    /**
     * Delete the Organisation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Organisation : {}", id);
        organisationRepository.findByIdOptional(new ObjectId(id)).ifPresent(organisation -> {
            organisationRepository.delete(organisation);
        });
    }

    /**
     * Get one organisation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<OrganisationDTO> findOne(String id) {
        log.debug("Request to get Organisation : {}", id);
        return organisationRepository.findByIdOptional(new ObjectId(id))
            .map(organisation -> organisationMapper.toDto((Organisation) organisation));
    }

    /**
     * Get all the organisations.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<OrganisationDTO> findAll(Page page) {
        log.debug("Request to get all Organisations");
        return new Paged<>(organisationRepository.findAll().page(page))
            .map(organisation -> organisationMapper.toDto((Organisation) organisation));
    }

    public OrganisationDTO createByName(String name) {
        log.debug("Request to create Organisation by name : {}", name);
        OrganisationDTO organisationDTO = new OrganisationDTO();
        organisationDTO.companyReg = "";
        organisationDTO.name = name;
        organisationDTO.address = "";
        organisationDTO.joinCode = RandomUtil.generateRandomString(7).toUpperCase();

        return this.persistOrUpdate(organisationDTO);
    }

    public Optional<OrganisationDTO> findByCode(String code) {
        log.debug("Request to get Organisation by code : {}", code);
        return organisationRepository.findOneByOrgCode(code)
            .map(organisation -> organisationMapper.toDto((Organisation) organisation));
    }

    public Optional<OrganisationMinimalDTO> findByCodeMinimal(String code) {
        log.debug("Request to get Organisation by code : {}", code);
        return organisationRepository.findOneByOrgCode(code)
            .map(organisation -> new OrganisationMinimalDTO((Organisation) organisation));
    }

    public DashboardComboDTO getOrganisationDashboard(String organisationId) {
        DashboardComboDTO dashboardCombo = new DashboardComboDTO();

        OrganisationDTO organisation = this.findOne(organisationId).orElseThrow();
        List<EmployeeDTO> employees = employeeService.findAllByOrganisation(organisation.id.toString());

        dashboardCombo.clients = clientService.findAllByOrganisation(organisation.id.toString());
        dashboardCombo.employeeOverview = new EmployeeOverviewDTO(employees.size(), 0);
        dashboardCombo.clientOverview = new ClientOverviewDTO(employeeAssignmentService.countAllMainByAssignmentType(AssignmentType.OUTSOURCE), employeeAssignmentService.countAllMainByAssignmentType(AssignmentType.PROJECT));
        dashboardCombo.projects = new ArrayList<>();

        return dashboardCombo;
    }

    public OrganisationBaseDTO getOrganisationBase(String organisationId) {
        OrganisationDTO organisation = this.findOne(organisationId).orElseThrow();
        List<EmployeeDTO> employees = employeeService.findAllByOrganisation(organisationId, Page.of(0, 5)).content;
        List<DepartmentDTO> departments = departmentService.findAllByOrganisation(organisationId);
        List<PositionEmployeeCountDTO> positions = positionService.findAllByPosition(organisationId);
        List<ClientEmployeeCountDTO> clients = clientService.findAllByOrganisation(organisationId, Page.of(0, 5)).content;
        List<AssignmentEmployeeCountDTO> assignments = assignmentService.findAllByOrganisation(organisationId, Page.of(0,5)).content;
        List<NewsletterDTO> newsletters = newsletterService.findAllByOrganisation(organisationId, Page.of(0, 5)).content;

        return new OrganisationBaseDTO(organisation, employees, departments, positions, clients, assignments, newsletters);
    }


}
