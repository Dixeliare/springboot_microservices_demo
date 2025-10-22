package com.example.CarManagementService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequestDTO {
    private String supplierName;
    private String supplierDescription;
    private String supplierAddress;

}
