package com.example.CustomerService.controllers;

import com.example.CustomerService.dtos.CustomerRequestDTO;
import com.example.CustomerService.dtos.CustomerResponseDTO;
import com.example.CustomerService.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Integer id) {
        CustomerResponseDTO customer =  customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomerResponseDTO> getCustomerByUserId(@PathVariable Integer userId) {
        CustomerResponseDTO customer = customerService.getCustomerByUserId(userId);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerRequestDTO customerRequestDTO) {
        CustomerResponseDTO customer = customerService.createCustomer(customerRequestDTO);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Integer id,
                                                              @RequestBody CustomerRequestDTO customerRequestDTO) {
        CustomerResponseDTO customer = customerService.updateCustomer(id, customerRequestDTO);
        return ResponseEntity.ok(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
