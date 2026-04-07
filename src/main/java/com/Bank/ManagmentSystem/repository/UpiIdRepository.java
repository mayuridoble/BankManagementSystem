package com.Bank.ManagmentSystem.repository;

import com.Bank.ManagmentSystem.entity.Upild;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UpiIdRepository extends JpaRepository<Upild, Long> {
    Optional<Upild> findByUpiHandle(String upiHandle);
    Optional<Upild> findByUserId(Long userId);
}