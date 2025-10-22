package com.example.CarManagementService.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supplier")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Integer supplierId;

    @Column(name = "supplier_name", unique = true, nullable = false)
    private String supplierName;

    @Column(name = "supplier_description")
    private String supplierDescription;

    @Column(name = "supplier_address")
    private String supplierAddress;
}
