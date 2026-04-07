package com.Bank.ManagmentSystem.controller;

import com.Bank.ManagmentSystem.dto.DepositWithdrawForm;
import com.Bank.ManagmentSystem.entity.Account;
import com.Bank.ManagmentSystem.entity.User;
import com.Bank.ManagmentSystem.service.AccountService;
import com.Bank.ManagmentSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/account")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/details")
    public String accountDetails(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Account account = accountService.findByUserId(user.getId());
        
        model.addAttribute("account", account);
        model.addAttribute("user", user);
        model.addAttribute("depositForm", new DepositWithdrawForm());
        model.addAttribute("withdrawForm", new DepositWithdrawForm());
        
        return "account";
    }
    
    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber,
                          @RequestParam BigDecimal amount,
                          @RequestParam(required = false) String description,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            Account account = accountService.findByUserId(user.getId());
            
            if (!account.getAccountNumber().equals(accountNumber)) {
                throw new RuntimeException("Invalid account number");
            }
            
            accountService.deposit(accountNumber, amount, description);
            redirectAttributes.addFlashAttribute("success", "Amount deposited successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/account/details";
    }
    
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber,
                           @RequestParam BigDecimal amount,
                           @RequestParam(required = false) String description,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            Account account = accountService.findByUserId(user.getId());
            
            if (!account.getAccountNumber().equals(accountNumber)) {
                throw new RuntimeException("Invalid account number");
            }
            
            accountService.withdraw(accountNumber, amount, description);
            redirectAttributes.addFlashAttribute("success", "Amount withdrawn successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/account/details";
    }
}