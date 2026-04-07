package com.Bank.ManagmentSystem.exception;

public class AccountNotFoundException extends BankException {
    
    public AccountNotFoundException(String message) {
        super(message, "ACCOUNT_NOT_FOUND");
    }
    
    public AccountNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}