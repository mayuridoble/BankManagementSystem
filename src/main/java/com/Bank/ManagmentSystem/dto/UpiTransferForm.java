package com.Bank.ManagmentSystem.dto;

import java.math.BigDecimal;

public class UpiTransferForm {
    
    private String fromUpiHandle;
    private String toUpiHandle;
    private BigDecimal amount;
    private String description;
    
    // Getters and Setters
    public String getFromUpiHandle() {
        return fromUpiHandle;
    }
    
    public void setFromUpiHandle(String fromUpiHandle) {
        this.fromUpiHandle = fromUpiHandle;
    }
    
    public String getToUpiHandle() {
        return toUpiHandle;
    }
    
    public void setToUpiHandle(String toUpiHandle) {
        this.toUpiHandle = toUpiHandle;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}