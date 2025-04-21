package com.devsu.hackerearth.backend.account.exception;

public class AccountInactiveException extends BusinessException {
    static final String MESSAGE = "Account with %s: %s %s";
    static final String ID = "ID: ";
    static final String NUMBER = "Number";
    static final String IS_ALREADY_INACTIVE = " is already inactive";
    static final String ACCOUNT_INACTIVE = "ACCOUNT_INACTIVE";
    private static final long serialVersionUID = -1920086539383480746L;

    public AccountInactiveException(Long id) {
        super(ACCOUNT_INACTIVE, String.format(MESSAGE, ID, id, IS_ALREADY_INACTIVE));
    }

    public AccountInactiveException(String number) {
        super(ACCOUNT_INACTIVE, String.format(MESSAGE, NUMBER, number, IS_ALREADY_INACTIVE));
    }
}