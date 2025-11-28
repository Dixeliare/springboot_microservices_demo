package com.example.RentingService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "car-management-service")
public interface CarServiceClient {

    @GetMapping("/api/v1/cars/{id}")
    ResponseEntity<Map<String, Object>> getCarById(@PathVariable("id") Integer carId);

    @PutMapping("/api/v1/cars/{id}/status")
    ResponseEntity<Map<String, Object>> updateCarStatus(@PathVariable("id") Integer carId, @RequestBody Map<String, String> statusUpdate);
}
