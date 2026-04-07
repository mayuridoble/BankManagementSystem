package com.Bank.ManagmentSystem.exception;

public class BankException extends RuntimeException {
    
    private String errorCode;
    
    public BankException(String message) {
        super(message);
    }
    
    public BankException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public BankException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}