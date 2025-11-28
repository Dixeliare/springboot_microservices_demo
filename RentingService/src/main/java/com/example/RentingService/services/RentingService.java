package com.example.RentingService.services;

import com.example.RentingService.dtos.RentingStatusUpdateDTO;
import com.example.RentingService.dtos.RentingTransactionRequestDTO;
import com.example.RentingService.dtos.RentingTransactionResponseDTO;
import com.example.RentingService.dtos.StatisticsResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface RentingService {
    RentingTransactionResponseDTO createTransaction(RentingTransactionRequestDTO request);
    RentingTransactionResponseDTO getTransactionById(Integer id);
    List<RentingTransactionResponseDTO> getTransactionsByCustomerId(Integer customerId);
    RentingTransactionResponseDTO updateStatus(Integer transactionId, RentingStatusUpdateDTO statusUpdate);
    void deleteTransaction(Integer transactionId);
    StatisticsResponseDTO getStatistics(LocalDate startDate, LocalDate endDate);
}
