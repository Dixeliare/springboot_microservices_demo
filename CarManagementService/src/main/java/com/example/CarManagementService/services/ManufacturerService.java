package com.example.CarManagementService.services;

import com.example.CarManagementService.dtos.ManufacturerRequestDTO;
import com.example.CarManagementService.dtos.ManufacturerResponseDTO;

import java.util.List;

public interface ManufacturerService {
    List<ManufacturerResponseDTO> getAllManufacturers();
    ManufacturerResponseDTO getManufacturerById(Integer manufacturerId);
    ManufacturerResponseDTO updateManufacturer(Integer manufacturerId, ManufacturerRequestDTO manufacturer);
    ManufacturerResponseDTO createManufacturer(ManufacturerRequestDTO manufacturer);
    void  deleteManufacturer(Integer manufacturerId);
}
