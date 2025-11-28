package com.example.RentingService.repositories;

import com.example.RentingService.models.RentingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentingTransactionRepository extends JpaRepository<RentingTransaction, Integer> {
    List<RentingTransaction> findByCustomerId(Integer customerId);
}
