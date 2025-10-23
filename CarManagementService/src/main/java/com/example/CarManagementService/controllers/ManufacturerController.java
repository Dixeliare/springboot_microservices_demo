package com.example.CarManagementService.controllers;

import com.example.CarManagementService.dtos.ManufacturerRequestDTO;
import com.example.CarManagementService.dtos.ManufacturerResponseDTO;
import com.example.CarManagementService.services.ManufacturerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/manufacturers")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<ManufacturerResponseDTO>> getManufacturers() {
        List<ManufacturerResponseDTO> manu = manufacturerService.getAllManufacturers();
        return ResponseEntity.ok(manu);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerResponseDTO> getManufacturer(@PathVariable Integer id) {
        ManufacturerResponseDTO manu = manufacturerService.getManufacturerById(id);
        return ResponseEntity.ok(manu);
    }

    @PostMapping
    public ResponseEntity<ManufacturerResponseDTO> createManufacturer(@RequestBody ManufacturerRequestDTO manufacturerRequestDTO) {
        ManufacturerResponseDTO manu  = manufacturerService.createManufacturer(manufacturerRequestDTO);
        return new ResponseEntity<>(manu, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerResponseDTO> updateManufacturer(@PathVariable Integer id, @RequestBody ManufacturerRequestDTO manufacturerRequestDTO) {
        ManufacturerResponseDTO manu = manufacturerService.updateManufacturer(id, manufacturerRequestDTO);
        return ResponseEntity.ok(manu);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ManufacturerResponseDTO> deleteManufacturer(@PathVariable Integer id) {
        manufacturerService.deleteManufacturer(id);
        return ResponseEntity.noContent().build();
    }
}
