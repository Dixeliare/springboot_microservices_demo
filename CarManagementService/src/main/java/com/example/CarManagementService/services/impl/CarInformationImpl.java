package com.example.CarManagementService.services.impl;

import com.example.CarManagementService.dtos.CarRequestDTO;
import com.example.CarManagementService.dtos.CarResponseDTO;
import com.example.CarManagementService.dtos.CarStatusUpdateRequestDTO;
import com.example.CarManagementService.exceptions.ResourceNotFoundException;
import com.example.CarManagementService.models.CarInformation;
import com.example.CarManagementService.models.Manufacturer;
import com.example.CarManagementService.models.Supplier;
import com.example.CarManagementService.repositories.CarInformationRepository;
import com.example.CarManagementService.repositories.ManufacturerRepository;
import com.example.CarManagementService.repositories.SupplierRepository;
import com.example.CarManagementService.services.CarInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarInformationImpl implements CarInformationService {
    private final CarInformationRepository carInformationRepository;
    private final SupplierRepository supplierRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CarResponseDTO> findAllAvailableCars() {
        return carInformationRepository
                .findByCarStatus("AVAILABLE")
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarResponseDTO> getAllCars() {
        return carInformationRepository
                .findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CarResponseDTO getCarById(Integer carId) {
        return carInformationRepository
                .findById(carId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "Id", carId.toString()));
    }

    @Override
    @Transactional
    public CarResponseDTO createCar(CarRequestDTO car) {
        Manufacturer manufacturer = manufacturerRepository
                .findById(car.getManufacturerId())
                .orElseThrow(() -> new  ResourceNotFoundException("Manufacturer", "Id", car.getManufacturerId().toString()));
        Supplier supplier = supplierRepository
                .findById(car.getSupplierId())
                .orElseThrow(() -> new  ResourceNotFoundException("Supplier", "Id", car.getSupplierId().toString()));
        CarInformation carInfo = new CarInformation(
                null,
                car.getCarName(),
                car.getCarDescription(),
                car.getNumberOfDoors(),
                car.getSeatingCapacity(),
                CarInformation.FuelType.valueOf(car.getCarStatus().toUpperCase()),
                car.getYear(),
                manufacturer,
                supplier,
                CarInformation.CarStatus.valueOf(car.getCarStatus().toUpperCase()),
                car.getCarRentingPricePerDay()
        );

        return convertToDto(carInformationRepository.save(carInfo));
    }

    @Override
    @Transactional
    public CarResponseDTO updateCar(Integer carId, CarRequestDTO request) {
        CarInformation car = carInformationRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", carId.toString()));

        Manufacturer manufacturer = manufacturerRepository.findById(request.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", request.getManufacturerId().toString()));
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", request.getSupplierId().toString()));

        car.setCarName(request.getCarName());
        car.setCarDescription(request.getCarDescription());
        car.setNumberOfDoors(request.getNumberOfDoors());
        car.setSeatingCapacity(request.getSeatingCapacity());
        car.setFuelType(CarInformation.FuelType.valueOf(request.getFuelType()));
        car.setYear(request.getYear());
        car.setManufacturer(manufacturer);
        car.setSupplier(supplier);
        car.setCarStatus(CarInformation.CarStatus.valueOf(request.getCarStatus()));
        car.setCarRentingPricePerDay(request.getCarRentingPricePerDay());

        return convertToDto(carInformationRepository.save(car));
    }

    @Override
    @Transactional
    public void deleteCar(Integer carId) {
        CarInformation car = carInformationRepository
                .findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "Id", carId.toString()));
        car.setCarStatus(CarInformation.CarStatus.SUSPENDED);
        carInformationRepository.save(car);
    }

    @Override
    @Transactional
    public CarResponseDTO updateCarStatus(Integer carId, CarStatusUpdateRequestDTO carStatus) {
        CarInformation car = carInformationRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", carId.toString()));

        car.setCarStatus(CarInformation.CarStatus.valueOf(carStatus.getCarStatus()));

        return convertToDto(carInformationRepository.save(car));
    }

    private CarResponseDTO  convertToDto(CarInformation carInformation) {
        return new CarResponseDTO(
                carInformation.getCarId(),
                carInformation.getCarName(),
                carInformation.getCarDescription(),
                carInformation.getNumberOfDoors(),
                carInformation.getSeatingCapacity(),
                carInformation.getFuelType().toString(),
                carInformation.getYear(),
                carInformation.getManufacturer().getManufacturerName(),
                carInformation.getManufacturer().getManufacturerCountry(),
                carInformation.getSupplier().getSupplierName(),
                carInformation.getCarStatus().toString(),
                carInformation.getCarRentingPricePerDay()
        );
    }
}