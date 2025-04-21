package com.devsu.hackerearth.backend.account.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.service.ReportService;
import com.devsu.hackerearth.backend.account.service.TransactionService;

import lombok.RequiredArgsConstructor;

/**
 * REST controller responsible for managing transaction operations and client
 * transaction reports.
 * <p>
 * Exposes endpoints for:
 * <ul>
 * <li>Registering new financial transactions.</li>
 * <li>Retrieving transaction history by account.</li>
 * <li>Initiating or polling client transaction reports.</li>
 * </ul>
 *
 * This controller delegates all business logic to dedicated service layers, in
 * compliance with
 * the Single Responsibility Principle (SRP). It acts strictly as an HTTP
 * adapter.
 *
 * Base path: <strong>/api/transactions</strong>
 *
 * Author: Germán Ponce
 * Version: 1.0
 */
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;
	private final ReportService reportService;

	/**
	 * Lists all transaction.
	 *
	 * @return HTTP 200 OK with list of active transaction DTOs
	 */
	@GetMapping
	public ResponseEntity<List<TransactionDto>> getAll() {
		return ResponseEntity.ok(transactionService.getAll());
	}

	/**
	 * Retrieves an transaction by its unique identifier.
	 *
	 * @param id transaction ID
	 * @return HTTP 200 OK with transaction DTO
	 */
	@GetMapping("/{id}")
	public ResponseEntity<TransactionDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(transactionService.getById(id));
	}

	/**
	 * Registers a new transaction for a given account.
	 *
	 * @param dto the transaction request body
	 * @return HTTP 201 Created with the persisted transaction
	 */
	@PostMapping
	public ResponseEntity<TransactionDto> create(@RequestBody TransactionDto transactionDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.create(transactionDto));
	}

	/**
	 * Retrieves a report for a given client and date range. If a correlation ID is
	 * provided,
	 * the report will be fetched. Otherwise, a new report request is dispatched.
	 *
	 * @param clientId             the ID of the client
	 * @param dateTransactionStart start date of the report range
	 * @param dateTransactionEnd   end date of the report range
	 * @param correlationId        optional identifier for polling an existing
	 *                             report
	 * @return HTTP 200 OK with the report, 202 if requested, or 404 if not found
	 */
	@GetMapping("/clients/{clientId}/report")
	public ResponseEntity<Object> getReport(
			@PathVariable Long clientId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTransactionStart,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTransactionEnd,
			@RequestParam(required = false) String correlationId) {

		return reportService.handleReportRequest(clientId, dateTransactionStart, dateTransactionEnd, correlationId);
	}

	/**
	 * Retrieves last transaction associated with a specific account.
	 *
	 * @param accountId the ID of the account
	 * @return HTTP 200 OK with a list of transactions
	 */
	@GetMapping("/account/{accountId}")
	public ResponseEntity<TransactionDto> getTransactionsByAccount(@PathVariable Long accountId) {
		return ResponseEntity.ok(transactionService.getLastByAccountId(accountId));
	}
}
