package com.example.CarManagementService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequestDTO {
    private String carName;
    private String carDescription;
    private Integer numberOfDoors;
    private Integer seatingCapacity;
    private String fuelType;
    private Integer year;
    private Integer manufacturerId;
    private Integer supplierId;
    private String carStatus;
    private Double carRentingPricePerDay;
}
