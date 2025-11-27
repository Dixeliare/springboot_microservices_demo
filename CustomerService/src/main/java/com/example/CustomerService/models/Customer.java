package com.example.CustomerService.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "email",unique = true, nullable = false)
    private String email;

    @Column(name = "telephone",unique = true, nullable = false)
    private String telephone;

    @Column(name = "customer_birthday")
    private LocalDate customerBirthday;

    @Column(name = "customer_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CustomerStatus customerStatus;

    @Column(name = "user_id", unique = true, nullable = false)
    private Integer userId;

    public enum CustomerStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }

}
