package com.devsu.hackerearth.backend.account.controller;

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

import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.service.AccountService;

/**
 * REST controller for exposing endpoints related to {@link Account} management.
 * <p>
 * Handles client requests for account creation, retrieval by ID or number,
 * listing active accounts,
 * and logical deletion (soft delete). All operations follow RESTful standards
 * and return appropriate
 * HTTP response codes.
 * </p>
 *
 * Base URL: <strong>/api/accounts</strong>
 *
 * Endpoints:
 * <ul>
 * <li><code>POST /api/accounts</code> – Create a new account.</li>
 * <li><code>GET /api/accounts/{id}</code> – Retrieve account by ID.</li>
 * <li><code>GET /api/accounts/number/{number}</code> – Retrieve account by
 * number.</li>
 * <li><code>GET /api/accounts/active</code> – List all active accounts.</li>
 * <li><code>DELETE /api/accounts/{id}</code> – Soft delete an account by
 * ID.</li>
 * </ul>
 *
 * Delegates business logic to {@link AccountService}.
 * 
 * Author: Germán Ponce
 * Version: 1.0
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * Lists all accounts currently marked as active.
	 *
	 * @return HTTP 200 OK with list of active account DTOs
	 */
	@GetMapping
	public ResponseEntity<List<AccountDto>> getAll() {
		return ResponseEntity.ok(accountService.getAll());
	}

	/**
	 * Retrieves an account by its unique identifier.
	 *
	 * @param id account ID
	 * @return HTTP 200 OK with account DTO
	 */
	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(accountService.getById(id));
	}

	@PostMapping
	public ResponseEntity<AccountDto> create(@RequestBody AccountDto accountDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(accountDto));
	}

	@PutMapping("/{id}")
	public ResponseEntity<AccountDto> update(@PathVariable Long id, @RequestBody AccountDto accountDto) {
		accountDto.setId(id);
		return ResponseEntity.ok(accountService.update(accountDto));
	}

	/**
	 * Applies a partial update to an existing account.
	 *
	 * @param id                the ID of the account to be updated
	 * @param partialAccountDto the fields to be updated
	 * @return HTTP 200 OK with the updated account DTO
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<AccountDto> partialUpdate(@PathVariable Long id,
			@RequestBody PartialAccountDto partialAccountDto) {
		return ResponseEntity.ok(accountService.partialUpdate(id, partialAccountDto));
	}

	/**
	 * Performs a fisical deletion of a account
	 *
	 * @param id the ID of the account to be deleted
	 * @return HTTP 204 No Content if the operation is successful
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		accountService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Retrieves an account by its account number.
	 *
	 * @param number account number
	 * @return HTTP 200 OK with account DTO
	 */
	@GetMapping("/number/{number}")
	public ResponseEntity<AccountDto> getByNumber(@PathVariable String number) {
		return ResponseEntity.ok(accountService.getByNumber(number));
	}
}
