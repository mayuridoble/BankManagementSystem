package com.Bank.ManagmentSystem.exception;

public class InsufficientBalanceException extends BankException {
    
    public InsufficientBalanceException(String message) {
        super(message, "INSUFFICIENT_BALANCE");
    }
    
    public InsufficientBalanceException(String message, String errorCode) {
        super(message, errorCode);
    }
}