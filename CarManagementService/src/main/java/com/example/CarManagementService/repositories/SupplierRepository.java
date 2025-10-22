package com.example.CarManagementService.repositories;

import com.example.CarManagementService.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Optional<Supplier> findBySupplierName(String supplierName);
}
