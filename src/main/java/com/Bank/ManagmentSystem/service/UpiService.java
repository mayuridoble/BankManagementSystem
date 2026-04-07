package com.Bank.ManagmentSystem.service;

import com.Bank.ManagmentSystem.entity.Account;
import com.Bank.ManagmentSystem.entity.Transaction;
import com.Bank.ManagmentSystem.entity.Upild;
import com.Bank.ManagmentSystem.entity.User;
import com.Bank.ManagmentSystem.repository.UpiIdRepository;
import com.Bank.ManagmentSystem.util.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UpiService {
    
    @Autowired
    private UpiIdRepository upildRepository;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private TransactionService transactionService;
    
    public Upild findByUpiHandle(String upiHandle) {
        return upildRepository.findByUpiHandle(upiHandle)
                .orElseThrow(() -> new RuntimeException("UPI ID not found: " + upiHandle));
    }
    
    public Upild findByUserId(Long userId) {
        return upildRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("UPI ID not found for user ID: " + userId));
    }
    
    public Upild findById(Long id) {
        return upildRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UPI ID not found with ID: " + id));
    }
    
    @Transactional
    public void transferViaUpi(String fromUpiHandle, String toUpiHandle, 
                               BigDecimal amount, String description) {
        Upild fromUpi = findByUpiHandle(fromUpiHandle);
        Upild toUpi = findByUpiHandle(toUpiHandle);
        
        Account fromAccount = fromUpi.getAccount();
        Account toAccount = toUpi.getAccount();
        
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance. Available: " + fromAccount.getBalance());
        }
        
        // Deduct from source account
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        accountService.saveAccount(fromAccount);
        
        // Add to destination account
        toAccount.setBalance(toAccount.getBalance().add(amount));
        accountService.saveAccount(toAccount);
        
        // Record transaction
        transactionService.recordTransaction(fromAccount, toAccount, amount, 
                                            description, Transaction.TransactionType.UPI_PAYMENT);
    }
    
    public byte[] generateQRCode(String upiHandle, int width, int height) {
        String upiData = "upi://pay?pa=" + upiHandle + "&pn=Bank&cu=INR";
        return QRCodeGenerator.generateQRCode(upiData, width, height);
    }
}