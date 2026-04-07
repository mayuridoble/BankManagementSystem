package com.Bank.ManagmentSystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "upi_ids")
public class Upild {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 100)
    private String upiHandle;
    
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUpiHandle() {
        return upiHandle;
    }
    
    public void setUpiHandle(String upiHandle) {
        this.upiHandle = upiHandle;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Account getAccount() {
        return account;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
}