package com.example.RentingService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentingDetailRequestDTO {
    private Integer carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
}
