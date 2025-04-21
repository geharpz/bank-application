package com.devsu.hackerearth.backend.client.exception;

public class ClientInactiveException extends BusinessException {
	static final String MESSAGE = "Client with %s: %s %s";
	static final String ID = "ID: ";
	static final String DNI = "DNI";
	static final String IS_ALREADY_INACTIVE = " is already inactive";
	private static final long serialVersionUID = -1920086539383480746L;

	static final String CLIENT_INACTIVE = "CLIENT_INACTIVE";

	public ClientInactiveException(Long id) {
		super(CLIENT_INACTIVE, String.format(MESSAGE, ID, id, IS_ALREADY_INACTIVE));
	}

	public ClientInactiveException(String dni) {
		super(CLIENT_INACTIVE, String.format(MESSAGE, DNI, dni, IS_ALREADY_INACTIVE));
	}
}