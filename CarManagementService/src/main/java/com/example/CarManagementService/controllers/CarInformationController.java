package com.example.CarManagementService.controllers;

import com.example.CarManagementService.dtos.CarRequestDTO;
import com.example.CarManagementService.dtos.CarResponseDTO;
import com.example.CarManagementService.dtos.CarStatusUpdateRequestDTO;
import com.example.CarManagementService.services.CarInformationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
public class CarInformationController {
    private final CarInformationService carInformationService;

    public CarInformationController(CarInformationService carInformationService) {
        this.carInformationService = carInformationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDTO> getCarById(@RequestParam Integer id){
        CarResponseDTO car = carInformationService.getCarById(id);
        return ResponseEntity.ok(car);
    }

    @GetMapping()
    public ResponseEntity<List<CarResponseDTO>> getAllCars(){
        List<CarResponseDTO> cars = carInformationService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/available")
    public ResponseEntity<List<CarResponseDTO>> getAllAvailableCars(){
        List<CarResponseDTO> cars = carInformationService.findAllAvailableCars();
        return ResponseEntity.ok(cars);
    }

    @PostMapping()
    public ResponseEntity<CarResponseDTO> createCar(@RequestBody CarRequestDTO car){
        CarResponseDTO carResponse = carInformationService.createCar(car);
        return ResponseEntity.ok(carResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponseDTO> updateCar(@PathVariable Integer id, @RequestBody CarRequestDTO car){
        CarResponseDTO carResponse = carInformationService.updateCar(id, car);
        return new ResponseEntity<>(carResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CarResponseDTO> updateCarStatus(@PathVariable Integer id, @RequestBody CarStatusUpdateRequestDTO car){
        CarResponseDTO carResponse = carInformationService.updateCarStatus(id, car);
        return new ResponseEntity<>(carResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CarResponseDTO> deleteCar(@RequestParam Integer id){
        carInformationService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
