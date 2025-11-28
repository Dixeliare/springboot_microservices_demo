package com.example.RentingService.repositories;

import com.example.RentingService.models.RentingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentingTransactionRepository extends JpaRepository<RentingTransaction, Integer> {
    List<RentingTransaction> findByCustomerId(Integer customerId);
    
    List<RentingTransaction> findByRentingDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT COUNT(t) FROM RentingTransaction t WHERE t.rentingDate BETWEEN :startDate AND :endDate")
    Long countByRentingDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(t) FROM RentingTransaction t WHERE t.rentingDate BETWEEN :startDate AND :endDate AND t.rentingStatus = :status")
    Long countByRentingDateBetweenAndStatus(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("status") RentingTransaction.RentingStatus status);
    
    @Query("SELECT COALESCE(SUM(t.totalPrice), 0) FROM RentingTransaction t WHERE t.rentingDate BETWEEN :startDate AND :endDate AND t.rentingStatus IN ('APPROVED', 'COMPLETED')")
    Double sumTotalPriceByRentingDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
