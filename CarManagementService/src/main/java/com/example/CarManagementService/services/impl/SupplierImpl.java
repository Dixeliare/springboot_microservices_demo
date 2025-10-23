package com.example.CarManagementService.services.impl;

import com.example.CarManagementService.dtos.SupplierRequestDTO;
import com.example.CarManagementService.dtos.SupplierResponseDTO;
import com.example.CarManagementService.exceptions.ResourceNotFoundException;
import com.example.CarManagementService.models.Supplier;
import com.example.CarManagementService.repositories.CarInformationRepository;
import com.example.CarManagementService.repositories.SupplierRepository;
import com.example.CarManagementService.services.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierImpl implements SupplierService {
    private final SupplierRepository supplierRepository;


    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> getAllSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDTO getSupplierById(Integer supplierId) {
        return supplierRepository.findById(supplierId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "Id", supplierId.toString()));
    }

    @Override
    @Transactional
    public SupplierResponseDTO createSupplier(SupplierRequestDTO supplier) {
        if(!supplierRepository.findBySupplierName(supplier.getSupplierName()).isPresent()) {
            throw new IllegalArgumentException("Supplier with name: " + supplier.getSupplierName() + " does not exist");
        }

        Supplier sup = new Supplier(
                null,
                supplier.getSupplierName(),
                supplier.getSupplierDescription(),
                supplier.getSupplierAddress()
        );

        return convertToDto(supplierRepository.save(sup));
    }

    @Override
    @Transactional
    public SupplierResponseDTO updateSupplier(Integer supplierId, SupplierRequestDTO supplier) {
        Supplier data = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "Id", supplierId.toString()));
        if(!data.getSupplierName().equals(supplier.getSupplierName()) &&
                supplierRepository.findBySupplierName(supplier.getSupplierName()).isPresent()) {
            throw new IllegalArgumentException("Supplier with name :" + supplier.getSupplierName() + " does not exist");
        }

        data.setSupplierName(supplier.getSupplierName());
        data.setSupplierAddress(supplier.getSupplierAddress());
        data.setSupplierDescription(supplier.getSupplierDescription());

        return convertToDto(supplierRepository.save(data));
    }

    @Override
    @Transactional
    public void deleteSupplier(Integer supplierId) {
        if(!supplierRepository.findById(supplierId).isPresent()) {
            throw new ResourceNotFoundException("Supplier", "Id", supplierId.toString());
        }
        supplierRepository.deleteById(supplierId);
    }

    private SupplierResponseDTO convertToDto(Supplier supplier) {
        return new SupplierResponseDTO(
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getSupplierDescription(),
                supplier.getSupplierAddress()
        );
    }
}
