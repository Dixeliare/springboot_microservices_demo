package com.example.CarManagementService.services;

import com.example.CarManagementService.dtos.ManufacturerRequestDTO;
import com.example.CarManagementService.dtos.ManufacturerResponseDTO;

import java.util.List;

public interface ManufacturerService {
    List<ManufacturerResponseDTO> getAllManufacturers();
    ManufacturerResponseDTO getManufacturerById(Integer manufacturerId);
    ManufacturerResponseDTO updateManufacturer(ManufacturerRequestDTO manufacturer);
    ManufacturerResponseDTO createManufacturer(Integer manufacturerId, ManufacturerRequestDTO manufacturer);
    void  deleteManufacturer(Integer manufacturerId);
}
