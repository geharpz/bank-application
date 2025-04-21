package com.devsu.hackerearth.backend.client.exception;

public class ClientNotFoundException extends BusinessException {
    static final String MESSAGE = "Client with %s: %s %s";
    static final String ID = "ID: ";
    static final String DNI = "DNI";
    static final String NOT_FOUND = " not found";
    static final String CLIENT_NOT_FOUND = "CLIENT_NOT_FOUND";
    private static final long serialVersionUID = -8959983771240628265L;

    public ClientNotFoundException(Long id) {
        super(CLIENT_NOT_FOUND, String.format(MESSAGE, ID, id, NOT_FOUND));
    }

    public ClientNotFoundException(String dni) {
        super(CLIENT_NOT_FOUND, String.format(MESSAGE, DNI, dni, NOT_FOUND));
    }
}