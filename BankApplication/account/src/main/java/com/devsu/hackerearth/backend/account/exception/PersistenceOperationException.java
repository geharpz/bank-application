package com.devsu.hackerearth.backend.account.exception;

public class PersistenceOperationException extends BusinessException {

    private static final long serialVersionUID = -2380238316585707866L;
    static final String PERSISTENCE_OPERATION = "PERSISTENCE_OPERATION";

    public PersistenceOperationException(String operation, String entity, Throwable cause) {
        super(PERSISTENCE_OPERATION,
                "persistence error during " + operation + " of " + entity + ": " + cause.getMessage());
        initCause(cause);
    }
}