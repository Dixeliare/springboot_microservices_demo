package com.example.RentingService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponseDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long totalTransactions;
    private Long approvedTransactions;
    private Long completedTransactions;
    private Long rejectedTransactions;
    private Long pendingTransactions;
    private Double totalRevenue;
    private Double averageTransactionValue;
}

