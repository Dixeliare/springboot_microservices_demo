package com.example.RentingService.services;

import com.example.RentingService.dtos.RentingStatusUpdateDTO;
import com.example.RentingService.dtos.RentingTransactionRequestDTO;
import com.example.RentingService.dtos.RentingTransactionResponseDTO;

import java.util.List;

public interface RentingService {
    RentingTransactionResponseDTO createTransaction(RentingTransactionRequestDTO request);
    RentingTransactionResponseDTO getTransactionById(Integer id);
    List<RentingTransactionResponseDTO> getTransactionsByCustomerId(Integer customerId);
    RentingTransactionResponseDTO updateStatus(Integer transactionId, RentingStatusUpdateDTO statusUpdate);
    void deleteTransaction(Integer transactionId);
}
