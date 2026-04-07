package com.Bank.ManagmentSystem.controller;

import com.Bank.ManagmentSystem.dto.UpiTransferForm;
import com.Bank.ManagmentSystem.entity.Account;
import com.Bank.ManagmentSystem.entity.User;
import com.Bank.ManagmentSystem.entity.Upild;
import com.Bank.ManagmentSystem.service.AccountService;
import com.Bank.ManagmentSystem.service.UpiService;
import com.Bank.ManagmentSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/upi")
public class UpiController {
    
    @Autowired
    private UpiService upiService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AccountService accountService;
    
    @GetMapping
    public String showUpiPage(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Upild upiId = upiService.findByUserId(user.getId());
        Account account = accountService.findByUserId(user.getId());
        
        model.addAttribute("upiId", upiId);
        model.addAttribute("account", account);
        model.addAttribute("upiTransferForm", new UpiTransferForm());
        model.addAttribute("user", user);
        
        return "qr";
    }
    
    @PostMapping("/transfer")
    public String upiTransfer(@Valid @ModelAttribute("upiTransferForm") UpiTransferForm upiTransferForm,
                              BindingResult bindingResult, Authentication authentication,
                              RedirectAttributes redirectAttributes, Model model) {
        
        if (bindingResult.hasErrors()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            Upild upiId = upiService.findByUserId(user.getId());
            Account account = accountService.findByUserId(user.getId());
            model.addAttribute("upiId", upiId);
            model.addAttribute("account", account);
            return "qr";
        }
        
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            Upild fromUpi = upiService.findByUserId(user.getId());
            
            // Verify from UPI handle matches the logged-in user's UPI
            if (!fromUpi.getUpiHandle().equals(upiTransferForm.getFromUpiHandle())) {
                throw new RuntimeException("Invalid source UPI handle");
            }
            
            upiService.transferViaUpi(
                upiTransferForm.getFromUpiHandle(),
                upiTransferForm.getToUpiHandle(),
                upiTransferForm.getAmount(),
                upiTransferForm.getDescription()
            );
            
            redirectAttributes.addFlashAttribute("success", 
                "UPI transfer of ₹" + upiTransferForm.getAmount() + " completed successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/upi";
    }
    
    @GetMapping(value = "/qrcode/{upiHandle}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> generateQRCode(@PathVariable String upiHandle) {
        try {
            byte[] qrCode = upiService.generateQRCode(upiHandle, 300, 300);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrCode);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/qrcode")
    public String showQRCode(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Upild upiId = upiService.findByUserId(user.getId());
        
        model.addAttribute("upiId", upiId);
        return "qrview";
    }
}