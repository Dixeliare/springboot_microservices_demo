package com.example.RentingService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentingTransactionResponseDTO {
    private Integer rentingTransactionId;
    private Integer customerId;
    private LocalDate rentingDate;
    private String rentingStatus;
    private Double totalPrice;
    private List<RentingDetailResponseDTO> rentingDetails;
}
