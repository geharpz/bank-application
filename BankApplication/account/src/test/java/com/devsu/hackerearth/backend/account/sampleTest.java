package com.devsu.hackerearth.backend.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.controller.AccountController;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.service.AccountService;
import com.devsu.hackerearth.backend.account.type.AccountType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class sampleTest {

	@Autowired
	private MockMvc mockMvc;

	private AccountService accountService = mock(AccountService.class);
	private AccountController accountController = new AccountController(accountService);

	@Test
	void createAccountTest() {
		// Arrange

		AccountDto newAccount = new AccountDto(1L, "number", AccountType.SAVINGS, BigDecimal.valueOf(0.0),
				BigDecimal.valueOf(0.0), true, 1L);
		AccountDto createdAccount = new AccountDto(1L, "number", AccountType.SAVINGS, BigDecimal.valueOf(0.0),
				BigDecimal.valueOf(0.0), true, 1L);
		when(accountService.create(newAccount)).thenReturn(createdAccount);

		// Act
		ResponseEntity<AccountDto> response = accountController.create(newAccount);

		// Assert
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(createdAccount, response.getBody());
	}

	/**
	 * Verifies that an {@link Account} object is constructed with all expected
	 * field values.
	 */
	@Test
	void shouldCreateAccountWithCorrectData() {
		Account account = new Account();
		account.setId(1L);
		account.setNumber("123-456");
		account.setType("AHORRO");
		account.setInitialAmount(new BigDecimal("1000.00"));
		account.setCurrentBalance(new BigDecimal("1000.00"));
		account.setClientId(7L);
		account.setActive(true);
		account.setCreatedBy("admin");
		account.setCreatedAt(LocalDateTime.now());

		assertEquals(1L, account.getId());
		assertEquals("123-456", account.getNumber());
		assertEquals("AHORRO", account.getType());
		assertEquals(new BigDecimal("1000.00"), account.getInitialAmount());
		assertEquals(new BigDecimal("1000.00"), account.getCurrentBalance());
		assertEquals(7L, account.getClientId());
		assertTrue(account.isActive());
		assertEquals("admin", account.getCreatedBy());
	}

	/**
	 * Ensures that the {@code isActive} flag can be toggled to false for
	 * deactivating an account.
	 */
	@Test
	void shouldDeactivateAccount() {
		Account account = new Account();
		account.setActive(true);

		account.setActive(false);

		assertFalse(account.isActive());
	}

	/**
	 * Performs an HTTP POST request to create an account and verifies the response
	 * payload and status code.
	 *
	 * @throws Exception if the request execution fails
	 */
	@Test
	void shouldCreateAccountSuccessfully() throws Exception {
		String requestBody = "{"
				+ "\"number\": \"1234567890\","
				+ "\"type\": \"SAVINGS\","
				+ "\"initialAmount\": 1000.00,"
				+ "\"currentBalance\": 1000.00,"
				+ "\"clientId\": 1"
				+ "}";

		mockMvc.perform(post("/api/accounts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.type").value("SAVINGS"))
				.andExpect(jsonPath("$.initialAmount").value(1000.00));
	}
}
