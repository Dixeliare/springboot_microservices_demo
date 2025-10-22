package com.example.CarManagementService.services.impl;

import com.example.CarManagementService.dtos.ManufacturerRequestDTO;
import com.example.CarManagementService.dtos.ManufacturerResponseDTO;
import com.example.CarManagementService.exceptions.ResourceNotFoundException;
import com.example.CarManagementService.models.Manufacturer;
import com.example.CarManagementService.repositories.ManufacturerRepository;
import com.example.CarManagementService.services.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManufacturerImpl implements ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ManufacturerResponseDTO> getAllManufacturers(){
        return manufacturerRepository.
                findAll().
                stream().
                map(this::convertDTO).
                toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ManufacturerResponseDTO getManufacturerById(Integer manufacturerId) {
        return manufacturerRepository.findById(manufacturerId).
                map(this::convertDTO).
                orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "Id", manufacturerId.toString()));
    }

    @Override
    public ManufacturerResponseDTO updateManufacturer(ManufacturerRequestDTO manufacturer) {
        return null;
    }

    @Override
    public ManufacturerResponseDTO createManufacturer(Integer manufacturerId, ManufacturerRequestDTO manufacturer) {
        return null;
    }

    @Override
    public void deleteManufacturer(Integer manufacturerId) {

    }

    private ManufacturerResponseDTO convertDTO(Manufacturer  manufacturer) {
        return new ManufacturerResponseDTO(
                manufacturer.getManufacturerId(),
                manufacturer.getManufacturerName(),
                manufacturer.getDescription(),
                manufacturer.getManufacturerCountry()
        );
    }
}
