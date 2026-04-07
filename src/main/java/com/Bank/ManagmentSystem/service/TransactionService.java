package com.Bank.ManagmentSystem.service;

import com.Bank.ManagmentSystem.entity.Account;
import com.Bank.ManagmentSystem.entity.Transaction;
import com.Bank.ManagmentSystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    @Lazy
    private AccountService accountService;
    
    @Transactional
    public void recordTransaction(Account fromAccount, Account toAccount, BigDecimal amount, 
                                   String description, Transaction.TransactionType type) {
        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setType(type);
        
        transactionRepository.save(transaction);
    }
    
    @Transactional
    public void transferMoney(String fromAccountNumber, String toAccountNumber, 
                              BigDecimal amount, String description) {
        Account fromAccount = accountService.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountService.findByAccountNumber(toAccountNumber);
        
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
        recordTransaction(fromAccount, toAccount, amount, description, Transaction.TransactionType.TRANSFER);
    }
    
    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}