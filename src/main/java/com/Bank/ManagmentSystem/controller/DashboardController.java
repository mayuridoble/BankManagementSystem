package com.Bank.ManagmentSystem.controller;

import com.Bank.ManagmentSystem.entity.Account;
import com.Bank.ManagmentSystem.entity.User;
import com.Bank.ManagmentSystem.entity.Upild;
import com.Bank.ManagmentSystem.service.AccountService;
import com.Bank.ManagmentSystem.service.UpiService;
import com.Bank.ManagmentSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

@Controller
public class DashboardController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private UpiService upiService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        System.out.println("=== Accessing dashboard ===");
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        System.out.println("Current username: " + username);
        
        try {
            // For default user (user), we need to handle gracefully
            if (username.equals("user")) {
                System.out.println("Default user detected - no database record");
                model.addAttribute("error", "Please register a new account to access dashboard features");
                model.addAttribute("user", new User());
                model.addAttribute("account", new Account());
                model.addAttribute("upiId", new Upild());
                model.addAttribute("balance", 0);
                return "dashboard";
            }
            
            User user = userService.findByUsername(username);
            Account account = accountService.findByUserId(user.getId());
            Upild upiId = upiService.findByUserId(user.getId());
            
            model.addAttribute("user", user);
            model.addAttribute("account", account);
            model.addAttribute("upiId", upiId);
            model.addAttribute("balance", account.getBalance());
            
            System.out.println("Dashboard loaded successfully for user: " + username);
            return "dashboard";
        } catch (Exception e) {
            System.out.println("Error loading dashboard: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
            return "error";
        }
    }
}