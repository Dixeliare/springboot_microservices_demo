package com.example.CarManagementService.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "car_information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Integer carId;

    @Column(name = "car_name", nullable = false)
    private String carName;

    @Column(name = "car_description")
    private String carDescription;

    @Column(name = "number_of_doors")
    private Integer numberOfDoors;

    @Column(name = "seating_capacity")
    private Integer seatingCapacity;

    @Column(name = "fuel_type")
    @Enumerated(EnumType.STRING)
    private FuelType fuelType; // e.g., "Petrol", "Diesel", "Electric"
    public enum FuelType {
        PETROL,
        DIESEL,
        ELECTRIC,
        HYBRID
    }

    @Column(name = "year")
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "car_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarStatus carStatus; // e.g., "AVAILABLE", "RENTED", "MAINTENANCE"
    public enum CarStatus {
        AVAILABLE,
        RENTED,
        MAINTENANCE,
        SUSPENDED
    }

    @Column(name = "car_renting_price_per_day", nullable = false)
    private Double carRentingPricePerDay;
}
