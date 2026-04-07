package com.Bank.ManagmentSystem.service;

import com.Bank.ManagmentSystem.entity.Account;
import com.Bank.ManagmentSystem.entity.Transaction;
import com.Bank.ManagmentSystem.repository.AccountRepository;
import com.Bank.ManagmentSystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    @Lazy  // Add this to break the circular dependency
    private TransactionService transactionService;
    
    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
    }
    
    public Account findByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Account not found for user ID: " + userId));
    }
    
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + id));
    }
    
    public BigDecimal getBalance(String accountNumber) {
        Account account = findByAccountNumber(accountNumber);
        return account.getBalance();
    }
    
    @Transactional
    public void deposit(String accountNumber, BigDecimal amount, String description) {
        Account account = findByAccountNumber(accountNumber);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        transactionService.recordTransaction(null, account, amount, description, Transaction.TransactionType.DEPOSIT);
    }
    
    @Transactional
    public void withdraw(String accountNumber, BigDecimal amount, String description) {
        Account account = findByAccountNumber(accountNumber);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        transactionService.recordTransaction(account, null, amount, description, Transaction.TransactionType.WITHDRAWAL);
    }
    
    @Transactional
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
}