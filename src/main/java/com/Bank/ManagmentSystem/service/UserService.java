package com.Bank.ManagmentSystem.service;

import com.Bank.ManagmentSystem.dto.RegistrationForm;
import com.Bank.ManagmentSystem.entity.Account;
import com.Bank.ManagmentSystem.entity.Upild;
import com.Bank.ManagmentSystem.entity.User;
import com.Bank.ManagmentSystem.repository.AccountRepository;
import com.Bank.ManagmentSystem.repository.UpiIdRepository;
import com.Bank.ManagmentSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private UpiIdRepository upiIdRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Transactional
    public User registerUser(RegistrationForm registrationForm) {
        System.out.println("========== STARTING REGISTRATION ==========");
        System.out.println("Username: " + registrationForm.getUsername());
        System.out.println("Email: " + registrationForm.getEmail());
        System.out.println("Full Name: " + registrationForm.getFullName());
        
        try {
            // Check if username or email already exists
            if (userRepository.existsByUsername(registrationForm.getUsername())) {
                System.out.println("ERROR: Username already exists: " + registrationForm.getUsername());
                throw new RuntimeException("Username already exists");
            }
            if (userRepository.existsByEmail(registrationForm.getEmail())) {
                System.out.println("ERROR: Email already exists: " + registrationForm.getEmail());
                throw new RuntimeException("Email already exists");
            }
            
            System.out.println("Username and email are available");
            
            // Create new user
            User user = new User();
            user.setUsername(registrationForm.getUsername());
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
            user.setEmail(registrationForm.getEmail());
            user.setFullName(registrationForm.getFullName());
            user.setRole(User.Role.USER);
            
            System.out.println("Saving user to database...");
            User savedUser = userRepository.save(user);
            System.out.println("User saved with ID: " + savedUser.getId());
            
            // Create account for the user
            System.out.println("Creating account...");
            Account account = new Account();
            account.setUser(savedUser);
            account = accountRepository.save(account);
            System.out.println("Account created with number: " + account.getAccountNumber());
            
            // Create UPI ID for the user
            System.out.println("Creating UPI ID...");
            Upild upiId = new Upild();
            upiId.setUser(savedUser);
            upiId.setAccount(account);
            upiId.setUpiHandle(savedUser.getUsername() + "@bank");
            upiId = upiIdRepository.save(upiId);
            System.out.println("UPI ID created: " + upiId.getUpiHandle());
            
            // Set account and upiId back to user
            savedUser.setAccount(account);
            savedUser.setUpiId(upiId);
            
            System.out.println("========== REGISTRATION COMPLETED SUCCESSFULLY ==========");
            return savedUser;
        } catch (Exception e) {
            System.out.println("========== REGISTRATION FAILED ==========");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
    
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }
}