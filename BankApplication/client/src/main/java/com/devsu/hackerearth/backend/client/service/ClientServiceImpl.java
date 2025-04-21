package com.devsu.hackerearth.backend.client.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;
import com.devsu.hackerearth.backend.client.exception.ClientInactiveException;
import com.devsu.hackerearth.backend.client.exception.ClientNotFoundException;
import com.devsu.hackerearth.backend.client.exception.DataConflictException;
import com.devsu.hackerearth.backend.client.exception.PersistenceOperationException;
import com.devsu.hackerearth.backend.client.mapper.ClientMapper;

import javax.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;

/**
 * Service layer responsible for encapsulating business logic related to
 * {@link Client} entities.
 * This class provides operations for client lifecycle management such as
 * creation, retrieval,
 * update, and logical deletion (soft delete). All operations ensure integrity
 * and transactional consistency.
 * <p>
 * It acts as a bridge between controller input and persistence layer, enforcing
 * validation,
 * security constraints (e.g., password encryption), and domain-specific
 * exception handling.
 * </p>
 * 
 * Dependencies:
 * <ul>
 * <li>{@link ClientRepository} for database access.</li>
 * <li>{@link ClientMapper} for transformation between entity and DTO
 * representations.</li>
 * <li>{@link PasswordEncoder} for secure password handling.</li>
 * </ul>
 * 
 * @author Germán Ponce
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
	private static final String CLIENT = "Client";
	private static final String RETRIEVE = "retrieve";

	private final ClientRepository clientRepository;
	private final ClientMapper clientMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public List<ClientDto> getAll() {
		try {
			return clientRepository.findAllActiveClientList().stream().map(clientMapper::toDto)
					.collect(Collectors.toList());
		} catch (PersistenceException e) {
			throw new PersistenceOperationException(RETRIEVE, CLIENT, e);
		}
	}

	@Override
	public ClientDto getById(Long id) {
		try {
			Client client = clientRepository.findActiveById(id).orElseThrow(() -> new ClientNotFoundException(id));
			return clientMapper.toDto(client);
		} catch (PersistenceException e) {
			throw new PersistenceOperationException(RETRIEVE, CLIENT, e);
		}
	}

	@Override
	public ClientDto create(ClientDto clientDto) {
		try {
			Client client = clientMapper.toEntity(clientDto);
			client.setPassword(passwordEncoder.encode(client.getPassword()));
			return clientMapper.toDto(clientRepository.save(client));
		} catch (DataIntegrityViolationException e) {
			throw new DataConflictException(CLIENT, "DNI or phone");
		} catch (PersistenceException e) {
			throw new PersistenceOperationException("create", CLIENT, e);
		}
	}

	@Override
	public ClientDto update(ClientDto clientDto) {
		Client client = clientRepository.findById(clientDto.getId())
				.orElseThrow(() -> new ClientNotFoundException(clientDto.getId()));

		if (!Boolean.TRUE.equals(client.isActive())) {
			throw new ClientInactiveException(clientDto.getId());
		}

		try {
			clientMapper.updateEntity(client, clientDto);

			if (client.getPassword() != null) {
				client.setPassword(passwordEncoder.encode(client.getPassword()));
			}
			return clientMapper.toDto(clientRepository.save(client));
		} catch (DataIntegrityViolationException e) {
			throw new DataConflictException(CLIENT, "Phone");
		} catch (PersistenceException e) {
			throw new PersistenceOperationException("update", CLIENT, e);
		}
	}

	@Override
	public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) {
		Client client = clientRepository.findById(id)
				.orElseThrow(() -> new ClientNotFoundException(id));

		if (!Boolean.TRUE.equals(client.isActive())) {
			throw new ClientInactiveException(id);
		}

		try {
			client.setActive(partialClientDto.isActive());
			return clientMapper.toDto(clientRepository.save(client));
		} catch (DataIntegrityViolationException e) {
			throw new DataConflictException(CLIENT, "Phone");
		} catch (PersistenceException e) {
			throw new PersistenceOperationException("update", CLIENT, e);
		}
	}

	@Override
	public void deleteById(Long id) {
		Client client = clientRepository.findById(id)
				.orElseThrow(() -> new ClientNotFoundException(id));
		clientRepository.delete(client);
	}

	@Override
	public ClientDto getByDni(String dni) {
		try {
			Client client = clientRepository.findActiveByDni(dni).orElseThrow(() -> new ClientNotFoundException(dni));
			return clientMapper.toDto(client);
		} catch (PersistenceException e) {
			throw new PersistenceOperationException(RETRIEVE, "Client by DNI", e);
		}
	}
}
