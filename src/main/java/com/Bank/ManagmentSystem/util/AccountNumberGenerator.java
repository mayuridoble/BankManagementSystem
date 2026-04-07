package com.Bank.ManagmentSystem.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AccountNumberGenerator {
    
    private static final String BANK_CODE = "BANK";
    
    public static String generateAccountNumber() {
        // Generate account number: BANK + timestamp + random number
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.valueOf((long) (Math.random() * 9000000000L));
        random = random.substring(0, Math.min(4, random.length()));
        
        return BANK_CODE + timestamp + random;
    }
    
    public static String generateUpiHandle(String username) {
        // Generate UPI handle: username@bank
        return username.toLowerCase() + "@bank";
    }
}