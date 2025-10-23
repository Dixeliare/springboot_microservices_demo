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
    public List<ManufacturerResponseDTO> getAllManufacturers() {
        return manufacturerRepository
                .findAll()
                .stream()
                .map(this::convertDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ManufacturerResponseDTO getManufacturerById(Integer manufacturerId) {
        return manufacturerRepository
                .findById(manufacturerId)
                .map(this::convertDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "Id", manufacturerId.toString()));
    }

    @Override
    @Transactional
    public ManufacturerResponseDTO updateManufacturer(Integer manufacturerId, ManufacturerRequestDTO manufacturer) {
        Manufacturer data = manufacturerRepository.findById(manufacturerId).
                orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "Id", manufacturerId.toString()));
        if (!data.getManufacturerName().equals(manufacturer.getManufacturerName()) &&
                manufacturerRepository.findByManufacturerName(manufacturer.getManufacturerName()).isPresent()) {
            throw new IllegalArgumentException("Manufacturer with name :" + manufacturer.getManufacturerName() + " already exists");
        }
        data.setManufacturerName(manufacturer.getManufacturerName());
        data.setDescription(manufacturer.getDescription());
        data.setManufacturerCountry(manufacturer.getManufacturerCountry());

        return convertDTO(manufacturerRepository.save(data));
    }

    @Override
    @Transactional
    public ManufacturerResponseDTO createManufacturer(ManufacturerRequestDTO manufacturer) {
        if (manufacturerRepository.findByManufacturerName(manufacturer.getManufacturerName()).isPresent()) {
            throw new IllegalArgumentException("Manufacturer with name " + manufacturer.getManufacturerName() + " already exists.");
        }
        Manufacturer manu = new Manufacturer(
                null,
                manufacturer.getManufacturerName(),
                manufacturer.getDescription(),
                manufacturer.getManufacturerCountry()
        );

        return convertDTO(manufacturerRepository.save(manu));
    }

    @Override
    public void deleteManufacturer(Integer manufacturerId) {
        if(!manufacturerRepository.existsById(manufacturerId)) {
            throw new ResourceNotFoundException("Manufacturer", "Id", manufacturerId.toString());
        }
        manufacturerRepository.deleteById(manufacturerId);

    }

    private ManufacturerResponseDTO convertDTO(Manufacturer manufacturer) {
        return new ManufacturerResponseDTO(
                manufacturer.getManufacturerId(),
                manufacturer.getManufacturerName(),
                manufacturer.getDescription(),
                manufacturer.getManufacturerCountry()
        );
    }
}
