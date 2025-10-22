package com.example.CarManagementService.services;

import com.example.CarManagementService.dtos.CarRequestDTO;
import com.example.CarManagementService.dtos.CarResponseDTO;
import com.example.CarManagementService.dtos.CarStatusUpdateRequestDTO;

import java.util.List;

public interface CarInformationService {
    List<CarResponseDTO> getAllCars();
    CarResponseDTO getCarById(Integer carId);
    CarResponseDTO createCar(CarRequestDTO car);
    CarResponseDTO updateCar(Integer carId, CarRequestDTO car);
    void deleteCar(Integer carId);

    List<CarResponseDTO> findAllAvailableCars();

    CarResponseDTO updateCarStatus(Integer carId, CarStatusUpdateRequestDTO carStatus);
}
