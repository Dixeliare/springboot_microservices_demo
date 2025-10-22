package com.example.CarManagementService.services;

import com.example.CarManagementService.dtos.SupplierRequestDTO;
import com.example.CarManagementService.dtos.SupplierResponseDTO;

import java.util.List;

public interface SupplierService {
    List<SupplierResponseDTO> getAllSuppliers();
    SupplierResponseDTO getSupplierById(Integer suplierId);
    SupplierResponseDTO createSupplier(SupplierRequestDTO supplier);
    SupplierResponseDTO updateSupplier(Integer supplierId, SupplierRequestDTO supplier);
    void deleteSupplier(Integer supplierId);
}
