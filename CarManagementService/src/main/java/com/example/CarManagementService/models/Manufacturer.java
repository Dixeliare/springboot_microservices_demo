package com.example.CarManagementService.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manufaturers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_id")
    private Integer manufacturerId;

    @Column(name = "manufacturer_name", unique = true, nullable = false)
    private String manufacturerName;

    @Column(name = "description")
    private String description;

    @Column(name = "manufacturer_country")
    private String manufacturerCountry;
}
