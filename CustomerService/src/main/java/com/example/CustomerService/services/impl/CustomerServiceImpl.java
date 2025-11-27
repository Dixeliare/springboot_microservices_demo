package com.example.CustomerService.services.impl;

import com.example.CustomerService.dtos.CustomerRequestDTO;
import com.example.CustomerService.dtos.CustomerResponseDTO;
import com.example.CustomerService.exceptions.ResourceNotFoundException;
import com.example.CustomerService.models.Customer;
import com.example.CustomerService.repositories.CustomerRepository;
import com.example.CustomerService.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers(){
        return customerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDTO getCustomerById(Integer customerId){
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerId", customerId.toString()));
        return convertToDTO(customer);
    }

    @Override
    public CustomerResponseDTO getCustomerByUserId(Integer userId){
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "userId", userId.toString()));
        return convertToDTO(customer);
    }

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
        Customer customer = convertToEntity(customerRequestDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(Integer customerId, CustomerRequestDTO customerRequestDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", customerId.toString()));

        // Update fields
        customer.setCustomerName(customerRequestDTO.getCustomerName());
        customer.setEmail(customerRequestDTO.getEmail());
        customer.setTelephone(customerRequestDTO.getTelephone());
        customer.setCustomerBirthday(customerRequestDTO.getCustomerBirthday());
        customer.setCustomerStatus(Customer.CustomerStatus.valueOf(customerRequestDTO.getCustomerStatus()));

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDTO(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", customerId.toString()));
        customerRepository.delete(customer);
    }

    private CustomerResponseDTO convertToDTO(Customer customer){
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setCustomerId(customer.getId());
        dto.setCustomerName(customer.getCustomerName());
        dto.setEmail(customer.getEmail());
        dto.setTelephone(customer.getTelephone());
        dto.setCustomerBirthday(customer.getCustomerBirthday());
        dto.setCustomerStatus(customer.getCustomerStatus().toString());
        dto.setUserId(customer.getUserId());
        return dto;
    }

    private  Customer convertToEntity(CustomerRequestDTO dto){
        Customer customer = new Customer();
        customer.setCustomerName(dto.getCustomerName());
        customer.setEmail(dto.getEmail());
        customer.setTelephone(dto.getTelephone());
        customer.setCustomerBirthday(dto.getCustomerBirthday());
        customer.setCustomerStatus(Customer.CustomerStatus.valueOf(dto.getCustomerStatus()));
        customer.setUserId(dto.getUserId());
        return customer;
    }
}
