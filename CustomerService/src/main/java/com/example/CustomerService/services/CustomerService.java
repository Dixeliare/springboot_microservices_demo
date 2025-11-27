package com.example.CustomerService.services;

import com.example.CustomerService.dtos.CustomerRequestDTO;
import com.example.CustomerService.dtos.CustomerResponseDTO;
import com.example.CustomerService.models.Customer;

import java.util.List;

public interface CustomerService {
    List<CustomerResponseDTO> getAllCustomers();
    CustomerResponseDTO getCustomerById(Integer customerId);
    CustomerResponseDTO getCustomerByUserId(Integer customerId);
    CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO);
    CustomerResponseDTO updateCustomer(Integer customerId, CustomerRequestDTO customerRequestDTO);
    void deleteCustomer(Integer customerId);
}