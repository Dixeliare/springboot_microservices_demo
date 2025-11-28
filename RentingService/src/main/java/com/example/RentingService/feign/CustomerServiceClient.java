package com.example.RentingService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "customer-service")
public interface CustomerServiceClient {

    @GetMapping("/api/v1/customers/{id}")
    ResponseEntity<Map<String, Object>> getCustomerById(@PathVariable("id") Integer customerId);
}
