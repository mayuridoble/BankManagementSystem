package com.Bank.ManagmentSystem.exception;

public class UserNotFoundException extends BankException {
    
    public UserNotFoundException(String message) {
        super(message, "USER_NOT_FOUND");
    }
    
    public UserNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}