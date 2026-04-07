package com.Bank.ManagmentSystem.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BankException.class)
    public String handleBankException(BankException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/dashboard";
    }
    
    @ExceptionHandler(InsufficientBalanceException.class)
    public String handleInsufficientBalanceException(InsufficientBalanceException ex, 
                                                      RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Insufficient balance: " + ex.getMessage());
        return "redirect:/transfer";
    }
    
    @ExceptionHandler(AccountNotFoundException.class)
    public String handleAccountNotFoundException(AccountNotFoundException ex, 
                                                  RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Account not found: " + ex.getMessage());
        return "redirect:/transfer";
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex, 
                                              RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "User not found: " + ex.getMessage());
        return "redirect:/login";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("error", "An unexpected error occurred: " + ex.getMessage());
        return "error";
    }
}