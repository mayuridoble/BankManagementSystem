package com.Bank.ManagmentSystem.controller;

import com.Bank.ManagmentSystem.dto.RegistrationForm;
import com.Bank.ManagmentSystem.entity.User;
import com.Bank.ManagmentSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String login(Model model, 
                        @RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout) {
        System.out.println("=== Login page accessed ===");
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        return "login";
    }
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        System.out.println("=== Registration page accessed ===");
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String fullName,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               Model model) {
        System.out.println("=== Processing registration for: " + username + " ===");
        
        if (!password.equals(confirmPassword)) {
            System.out.println("ERROR: Passwords do not match");
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("registrationForm", new RegistrationForm());
            return "register";
        }
        
        try {
            RegistrationForm registrationForm = new RegistrationForm();
            registrationForm.setUsername(username);
            registrationForm.setEmail(email);
            registrationForm.setFullName(fullName);
            registrationForm.setPassword(password);
            registrationForm.setConfirmPassword(confirmPassword);
            
            User user = userService.registerUser(registrationForm);
            System.out.println("Registration successful! User ID: " + user.getId());
            model.addAttribute("success", "Registration successful! Please login.");
            return "login";
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            model.addAttribute("registrationForm", new RegistrationForm());
            return "register";
        }
    }
    
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }
}