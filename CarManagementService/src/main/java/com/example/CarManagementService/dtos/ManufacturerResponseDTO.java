package com.example.CarManagementService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerResponseDTO {
    private Integer manufacturerId;
    private String manufacturerName;
    private String description;
    private String manufacturerCountry;
}
