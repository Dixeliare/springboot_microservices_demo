package com.example.CarManagementService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarResponseDTO {
    private Integer carId;
    private String carName;
    private String carDescription;
    private Integer numberOfDoors;
    private Integer seatingCapacity;
    private String fuelType;
    private Integer year;
    private String manufacturerName; // Display name, loaded from Manufacturer entity
    private String manufacturerCountry; // Example of more detail from Manufacturer
    private String supplierName;     // Display name, loaded from Supplier entity
    private String carStatus;
    private Double carRentingPricePerDay;
}
