package com.example.RentingService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentingTransactionRequestDTO {
    private Integer customerId;
    private List<RentingDetailRequestDTO> rentingDetails;
}
