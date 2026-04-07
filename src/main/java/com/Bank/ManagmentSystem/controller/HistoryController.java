package com.Bank.ManagmentSystem.controller;

import com.Bank.ManagmentSystem.entity.Account;
import com.Bank.ManagmentSystem.entity.Transaction;
import com.Bank.ManagmentSystem.entity.User;
import com.Bank.ManagmentSystem.service.AccountService;
import com.Bank.ManagmentSystem.service.TransactionService;
import com.Bank.ManagmentSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryController {
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String transactionHistory(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Account account = accountService.findByUserId(user.getId());
        
        List<Transaction> transactions = transactionService.getTransactionHistory(account.getId());
        
        model.addAttribute("transactions", transactions);
        model.addAttribute("account", account);
        model.addAttribute("user", user);
        
        return "history";
    }
}