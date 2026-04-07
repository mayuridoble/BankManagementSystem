package com.Bank.ManagmentSystem.controller;

import com.Bank.ManagmentSystem.entity.Account;
import com.Bank.ManagmentSystem.entity.User;
import com.Bank.ManagmentSystem.service.AccountService;
import com.Bank.ManagmentSystem.service.TransactionService;
import com.Bank.ManagmentSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/transfer")
public class TransferController {
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String showTransferForm(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Account account = accountService.findByUserId(user.getId());
        
        model.addAttribute("fromAccount", account);
        model.addAttribute("user", user);
        // DO NOT add transferForm - we are using simple HTML forms
        
        return "transfer";
    }
    
    @PostMapping
    public String processTransfer(@RequestParam String fromAccountNumber,
                                  @RequestParam String toAccountNumber,
                                  @RequestParam BigDecimal amount,
                                  @RequestParam(required = false) String description,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            Account fromAccount = accountService.findByUserId(user.getId());
            
            if (!fromAccount.getAccountNumber().equals(fromAccountNumber)) {
                throw new RuntimeException("Invalid source account");
            }
            
            Account toAccount = accountService.findByAccountNumber(toAccountNumber);
            
            if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
                throw new RuntimeException("Cannot transfer to the same account");
            }
            
            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance. Available: ₹" + fromAccount.getBalance());
            }
            
            transactionService.transferMoney(fromAccountNumber, toAccountNumber, amount, description);
            
            redirectAttributes.addFlashAttribute("success", "Transfer of ₹" + amount + " completed successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/transfer";
    }
}