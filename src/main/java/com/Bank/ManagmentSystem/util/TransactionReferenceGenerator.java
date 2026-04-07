package com.Bank.ManagmentSystem.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TransactionReferenceGenerator {
    
    private static long counter = 1;
    
    public static synchronized String generateReferenceNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String sequence = String.format("%06d", counter++ % 1000000);
        
        return "TXN" + timestamp + sequence;
    }
    
    public static void resetCounter() {
        counter = 1;
    }
}