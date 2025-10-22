package com.example.CarManagementService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerRequestDTO {
    private String manufacturerName;
    private String description;
    private String manufacturerCountry;
}
