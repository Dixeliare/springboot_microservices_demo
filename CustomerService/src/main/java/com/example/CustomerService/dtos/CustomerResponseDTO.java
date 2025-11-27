package com.example.CustomerService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTO {
    private Integer customerId;
    private String customerName;
    private String email;
    private String telephone;
    private LocalDate customerBirthday;
    private String customerStatus;
    private Integer userId;
}
