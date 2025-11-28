package com.example.RentingService.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "renting_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "renting_detail_id")
    private Integer rentingDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renting_transaction_id", nullable = false)
    private RentingTransaction rentingTransaction;

    @Column(name = "car_id", nullable = false)
    private Integer carId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "price", nullable = false)
    private Double price;
}