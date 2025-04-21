package com.devsu.hackerearth.backend.client.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.service.ClientService;

/**
 * REST controller that exposes endpoints for managing {@link Client} resources.
 * Acts as the entry point for HTTP clients, mapping requests to business logic
 * in {@link ClientService}.
 * <p>
 * Supports full client lifecycle operations including creation, retrieval by ID
 * or DNI, partial updates,
 * listing active clients, and logical deletion.
 * </p>
 * 
 * All endpoints follow RESTful conventions and return appropriate HTTP status
 * codes.
 * Validation is applied to input DTOs where required, and URI location headers
 * are provided on resource creation.
 * 
 * Base path: <strong>/api/clients</strong>
 * 
 * <ul>
 * <li><code>POST /api/clients</code> - Create client</li>
 * <li><code>GET /api/clients/{id}</code> - Get client by ID</li>
 * <li><code>GET /api/clients/dni/{dni}</code> - Get client by DNI</li>
 * <li><code>GET /api/clients/active</code> - List all active clients</li>
 * <li><code>PATCH /api/clients/{id}</code> - Update client</li>
 * <li><code>DELETE /api/clients/{id}</code> - Deactivate client</li>
 * </ul>
 * 
 * @author Germán Ponce
 * @version 1.0
 */
@RestController
@RequestMapping("/api/clients")
public class ClientController {

	private final ClientService clientService;

	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}

	@GetMapping
	public ResponseEntity<List<ClientDto>> getAll() {
		return ResponseEntity.ok(clientService.getAll());
	}

	/**
	 * Retrieves a client by its unique identifier.
	 *
	 * @param id the client ID
	 * @return HTTP 200 OK with the client DTO
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ClientDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(clientService.getById(id));
	}

	/**
	 * Creates a new client based on the input DTO. The created resource location is
	 * returned in the response header.
	 *
	 * @param clientDto  the input client data
	 * @param uriBuilder utility to build resource URI
	 * @return HTTP 201 Created with the created client in the response body
	 */
	@PostMapping
	public ResponseEntity<ClientDto> create(@RequestBody ClientDto clientDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body((clientService.create(clientDto)));
	}

	/**
	 * Applies a update to an existing client.
	 *
	 * @param id        the ID of the client to be updated
	 * @param clientDto the fields to be updated
	 * @return HTTP 200 OK with the updated client DTO
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ClientDto> update(@PathVariable Long id, @RequestBody ClientDto clientDto) {
		clientDto.setId(id);
		return ResponseEntity.ok(clientService.update(clientDto));
	}

	/**
	 * Applies a partial update to an existing client.
	 *
	 * @param id               the ID of the client to be updated
	 * @param partialClientDto the fields to be updated
	 * @return HTTP 200 OK with the updated client DTO
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<ClientDto> partialUpdate(@PathVariable Long id,
			@RequestBody PartialClientDto partialClientDto) {
		return ResponseEntity.ok(clientService.partialUpdate(id, partialClientDto));
	}

	/**
	 * Performs a fisical deletion (soft delete) of a client
	 *
	 * @param id the ID of the client to be deleted
	 * @return HTTP 204 No Content if the operation is successful
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		clientService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Retrieves a client by its DNI (national identity number).
	 *
	 * @param dni the client's DNI
	 * @return HTTP 200 OK with the client DTO
	 */
	@GetMapping("/dni/{dni}")
	public ResponseEntity<ClientDto> getByDni(@PathVariable String dni) {
		return ResponseEntity.ok(clientService.getByDni(dni));
	}
}
