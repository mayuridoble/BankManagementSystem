package com.Bank.ManagmentSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.Bank.ManagmentSystem.entity")
@EnableJpaRepositories(basePackages = "com.Bank.ManagmentSystem.repository")
public class ManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagementSystemApplication.class, args);
    }
}