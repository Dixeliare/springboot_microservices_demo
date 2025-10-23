package com.example.CarManagementService.controllers;

import com.example.CarManagementService.dtos.SupplierRequestDTO;
import com.example.CarManagementService.dtos.SupplierResponseDTO;
import com.example.CarManagementService.models.Supplier;
import com.example.CarManagementService.services.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/supplier")
public class SupplierController {
    private final SupplierService supplierService;

    public  SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping()
    public ResponseEntity<List<SupplierResponseDTO>> getAllSuppliers() {
        List<SupplierResponseDTO> sup = supplierService.getAllSuppliers();
        return ResponseEntity.ok(sup);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getSupplierById(@PathVariable Integer id) {
        SupplierResponseDTO sup = supplierService.getSupplierById(id);
        return ResponseEntity.ok(sup);
    }

    @PostMapping()
    public ResponseEntity<SupplierResponseDTO>  createSupplier(@RequestBody SupplierRequestDTO supplierRequestDTO) {
        SupplierResponseDTO sup = supplierService.createSupplier(supplierRequestDTO);
        return new ResponseEntity<>(sup, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> updateSupplierById(@PathVariable Integer id, @RequestBody SupplierRequestDTO supplierRequestDTO) {
        SupplierResponseDTO sup = supplierService.updateSupplier(id, supplierRequestDTO);
        return ResponseEntity.ok(sup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> deleteSupplierById(@PathVariable Integer id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
