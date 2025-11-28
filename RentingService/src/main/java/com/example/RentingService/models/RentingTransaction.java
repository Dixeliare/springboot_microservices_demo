package com.example.RentingService.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "renting_transactions")
public class RentingTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "renting_transaction_id")
    private Integer rentingTransactionId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "renting_date", nullable = false)
    private LocalDate rentingDate;

    @Column(name = "renting_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RentingStatus rentingStatus;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @OneToMany(mappedBy = "rentingTransaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentingDetail> rentingDetails = new ArrayList<>();

    public enum RentingStatus {
        PENDING,
        APPROVED,
        COMPLETED,
        REJECTED
    }
}
