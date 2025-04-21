package com.devsu.hackerearth.backend.account.exception;

public class InsufficientBalanceException extends BusinessException {

    static final String INSUFFICIENT_FUNDS = "INSUFFICIENT_FUNDS";
    private static final long serialVersionUID = 8597876535786876011L;

    public InsufficientBalanceException(String accountNumber) {
        super(INSUFFICIENT_FUNDS, "Balance not available for the account: " + accountNumber);
    }
}