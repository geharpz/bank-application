package com.devsu.hackerearth.backend.account.exception;

public class AccountNotFoundException extends BusinessException {
	static final String MESSAGE = "Account with %s: %s %s";
	static final String ID = "ID: ";
	static final String NUMBER = "Number";
	static final String NOT_FOUND = " not found";
	static final String ACCOUNT_NOT_FOUND = "ACCOUNT_NOT_FOUND";
	private static final long serialVersionUID = -8959983771240628265L;

	public AccountNotFoundException(Long id) {
		super(ACCOUNT_NOT_FOUND, String.format(MESSAGE, ID, id, NOT_FOUND));
	}

	public AccountNotFoundException(String dni) {
		super(ACCOUNT_NOT_FOUND, String.format(MESSAGE, NUMBER, dni, NOT_FOUND));
	}
}