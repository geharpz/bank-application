package com.devsu.hackerearth.backend.account.exception;

public class DataConflictException extends BusinessException {

    private static final long serialVersionUID = 2703352017990262420L;

    static final String DATA_CONFLICT = "DATA_CONFLICT";

    public DataConflictException(String entity, String field) {
        super(DATA_CONFLICT, "Conflict in " + entity + ": duplicate or invalid " + field);
    }
}