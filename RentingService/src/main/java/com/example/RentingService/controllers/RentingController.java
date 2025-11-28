package com.example.RentingService.controllers;

import com.example.RentingService.dtos.RentingStatusUpdateDTO;
import com.example.RentingService.dtos.RentingTransactionRequestDTO;
import com.example.RentingService.dtos.RentingTransactionResponseDTO;
import com.example.RentingService.services.RentingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rentings")
public class RentingController {

    private final RentingService rentingService;

    public RentingController(RentingService rentingService) {
        this.rentingService = rentingService;
    }

    @PostMapping
    public ResponseEntity<RentingTransactionResponseDTO> create(@RequestBody RentingTransactionRequestDTO request) {
        return new ResponseEntity<>(rentingService.createTransaction(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentingTransactionResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(rentingService.getTransactionById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<RentingTransactionResponseDTO>> getByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(rentingService.getTransactionsByCustomerId(customerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RentingTransactionResponseDTO> updateStatus(
            @PathVariable Integer id,
            @RequestBody RentingStatusUpdateDTO statusUpdate) {
        return ResponseEntity.ok(rentingService.updateStatus(id, statusUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        rentingService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
