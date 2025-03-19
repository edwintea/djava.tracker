package com.trackr.service;

import com.trackr.service.dto.ClientEmployeeCountDTO;
import com.trackr.service.dto.ClientMinimalDTO;
import io.quarkus.panache.common.Page;
import com.trackr.domain.Client;
import com.trackr.repository.ClientRepository;
import com.trackr.service.dto.ClientDTO;
import com.trackr.service.mapper.ClientMapper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientService.class);

    @Inject
    ClientRepository clientRepository;

    @Inject
    ClientMapper clientMapper;

    @Inject
    EmployeeService employeeService;

    @Inject
    EmployeeAssignmentService employeeAssignmentService;

    public ClientDTO persistOrUpdate(ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO);
        var client = clientMapper.toEntity(clientDTO);
        clientRepository.persistOrUpdate(client);
        return clientMapper.toDto(client);
    }

    /**
     * Delete the Client by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.findByIdOptional(new ObjectId(id)).ifPresent(client -> {
            clientRepository.delete(client);
        });
    }

    /**
     * Get one client by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<ClientDTO> findOne(String id) {
        log.debug("Request to get Client : {}", id);
        return clientRepository.findByIdOptional(new ObjectId(id))
            .map(client -> clientMapper.toDto((Client) client));
    }

    /**
     * Get all the clients.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<ClientDTO> findAll(Page page) {
        log.debug("Request to get all Clients");
        return new Paged<>(clientRepository.findAll().page(page))
            .map(client -> clientMapper.toDto((Client) client));
    }

    public List<ClientEmployeeCountDTO> findAllByOrganisation(String organisationId) {
        log.debug("Request to get all Clients by organisation");
        return clientRepository.findAllByOrganisation(organisationId)
            .stream().map(client -> new ClientEmployeeCountDTO(clientMapper.toDto(client), employeeAssignmentService.countAllMainByClient(client.id.toString()))).collect(Collectors.toList());
    }

    public List<ClientMinimalDTO> findAllMinimalByOrganisation(String organisationId) {
        return this.findAllByOrganisation(organisationId).stream().map(ClientMinimalDTO::new).collect(Collectors.toList());
    }

    public Paged<ClientEmployeeCountDTO> findAllByOrganisation(String organisationId, Page page) {
        log.debug("Request to get all Clients by Organisation");
        return new Paged<>(clientRepository.findPageableClientByOrganisation(organisationId, page))
            .map(client -> new ClientEmployeeCountDTO(clientMapper.toDto(client), employeeAssignmentService.countAllMainByClient(client.id.toString())));
    }



}
