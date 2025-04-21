package com.devsu.hackerearth.backend.account.exception;

public class TransactionNotFoundException extends BusinessException {
    static final String MESSAGE = "Transaction with %s: %s %s";
    static final String ID = "ID: ";
    static final String NOT_FOUND  = " not found";
    static final String TRANSACTION_NOT_FOUND = "TRANSACTION_NOT_FOUND";
      private static final long serialVersionUID = - 8959983771240628265L;
  
      public TransactionNotFoundException(Long id) {
          super(TRANSACTION_NOT_FOUND, String.format(MESSAGE, ID, id , NOT_FOUND));
      }
      
  }