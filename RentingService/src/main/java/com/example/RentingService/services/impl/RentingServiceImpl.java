package com.example.RentingService.services.impl;

import com.example.RentingService.dtos.*;
import com.example.RentingService.feign.CarServiceClient;
import com.example.RentingService.feign.CustomerServiceClient;
import org.springframework.http.ResponseEntity;
import java.util.Map;

import com.example.RentingService.exceptions.ResourceNotFoundException;
import com.example.RentingService.models.RentingDetail;
import com.example.RentingService.models.RentingTransaction;
import com.example.RentingService.repositories.RentingTransactionRepository;
import com.example.RentingService.services.RentingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentingServiceImpl implements RentingService {

    private final RentingTransactionRepository transactionRepository;
    private final CarServiceClient carServiceClient;
    private final CustomerServiceClient customerServiceClient;

    public RentingServiceImpl(RentingTransactionRepository transactionRepository,
                              CarServiceClient carServiceClient,
                              CustomerServiceClient customerServiceClient) {
        this.transactionRepository = transactionRepository;
        this.carServiceClient = carServiceClient;
        this.customerServiceClient = customerServiceClient;
    }

    @Override
    public RentingTransactionResponseDTO createTransaction(RentingTransactionRequestDTO request) {
        // 1. Validate customer tồn tại
        try {
            ResponseEntity<Map<String, Object>> customerResponse =
                    customerServiceClient.getCustomerById(request.getCustomerId());

            if (customerResponse.getStatusCode().isError() || customerResponse.getBody() == null) {
                throw new IllegalArgumentException("Customer not found with ID: " + request.getCustomerId());
            }
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new IllegalArgumentException("Customer not found with ID: " + request.getCustomerId());
        }

        // 2. Validate từng car và check availability
        for (RentingDetailRequestDTO detailDto : request.getRentingDetails()) {
            try {
                ResponseEntity<Map<String, Object>> carResponse =
                        carServiceClient.getCarById(detailDto.getCarId());

                if (carResponse.getStatusCode().isError() || carResponse.getBody() == null) {
                    throw new IllegalArgumentException("Car not found with ID: " + detailDto.getCarId());
                }

                Map<String, Object> car = carResponse.getBody();
                String carStatus = (String) car.get("carStatus");

                if (!"AVAILABLE".equals(carStatus)) {
                    throw new IllegalArgumentException(
                            "Car ID " + detailDto.getCarId() + " is not available. Current status: " + carStatus
                    );
                }
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalArgumentException("Car not found with ID: " + detailDto.getCarId());
            }
        }

        // 3. Tạo transaction
        RentingTransaction transaction = new RentingTransaction();
        transaction.setCustomerId(request.getCustomerId());
        transaction.setRentingDate(LocalDate.now());
        transaction.setRentingStatus(RentingTransaction.RentingStatus.PENDING);

        List<RentingDetail> details = request.getRentingDetails().stream()
                .map(detailDto -> {
                    RentingDetail detail = new RentingDetail();
                    detail.setCarId(detailDto.getCarId());
                    detail.setStartDate(detailDto.getStartDate());
                    detail.setEndDate(detailDto.getEndDate());
                    detail.setPrice(detailDto.getPrice());
                    detail.setRentingTransaction(transaction);
                    return detail;
                }).collect(Collectors.toList());

        transaction.setRentingDetails(details);
        double totalPrice = details.stream().mapToDouble(RentingDetail::getPrice).sum();
        transaction.setTotalPrice(totalPrice);

        RentingTransaction saved = transactionRepository.save(transaction);
        return convertToResponse(saved);
    }

    @Override
    public RentingTransactionResponseDTO getTransactionById(Integer id) {
        RentingTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RentingTransaction", "id", id.toString()));
        return convertToResponse(transaction);
    }

    @Override
    public List<RentingTransactionResponseDTO> getTransactionsByCustomerId(Integer customerId) {
        return transactionRepository.findByCustomerId(customerId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RentingTransactionResponseDTO updateStatus(Integer transactionId, RentingStatusUpdateDTO statusUpdate) {
        RentingTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("RentingTransaction", "id", transactionId.toString()));

        String newStatus = statusUpdate.getRentingStatus();
        RentingTransaction.RentingStatus status = RentingTransaction.RentingStatus.valueOf(newStatus);

        // Nếu approve transaction, update car status thành RENTED
        if (status == RentingTransaction.RentingStatus.APPROVED) {
            for (RentingDetail detail : transaction.getRentingDetails()) {
                try {
                    Map<String, String> statusUpdateMap = Map.of("carStatus", "RENTED");
                    carServiceClient.updateCarStatus(detail.getCarId(), statusUpdateMap);
                } catch (Exception e) {
                    // Log error nhưng không throw để transaction vẫn được update
                    System.err.println("Failed to update car status for carId: " + detail.getCarId() + ": " + e.getMessage());
                }
            }
        }

        // Nếu reject hoặc complete, set car về AVAILABLE
        if (status == RentingTransaction.RentingStatus.REJECTED ||
                status == RentingTransaction.RentingStatus.COMPLETED) {
            for (RentingDetail detail : transaction.getRentingDetails()) {
                try {
                    Map<String, String> statusUpdateMap = Map.of("carStatus", "AVAILABLE");
                    carServiceClient.updateCarStatus(detail.getCarId(), statusUpdateMap);
                } catch (Exception e) {
                    System.err.println("Failed to update car status for carId: " + detail.getCarId() + ": " + e.getMessage());
                }
            }
        }

        transaction.setRentingStatus(status);
        RentingTransaction updated = transactionRepository.save(transaction);
        return convertToResponse(updated);
    }

    @Override
    public void deleteTransaction(Integer transactionId) {
        RentingTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("RentingTransaction", "id", transactionId.toString()));
        transactionRepository.delete(transaction);
    }

    private RentingTransactionResponseDTO convertToResponse(RentingTransaction transaction) {
        RentingTransactionResponseDTO response = new RentingTransactionResponseDTO();
        response.setRentingTransactionId(transaction.getRentingTransactionId());
        response.setCustomerId(transaction.getCustomerId());
        response.setRentingDate(transaction.getRentingDate());
        response.setRentingStatus(transaction.getRentingStatus().name());
        response.setTotalPrice(transaction.getTotalPrice());

        List<RentingDetailResponseDTO> detailResponses = transaction.getRentingDetails().stream()
                .map(this::convertDetailToResponse)
                .collect(Collectors.toList());
        response.setRentingDetails(detailResponses);
        return response;
    }

    private RentingDetailResponseDTO convertDetailToResponse(RentingDetail detail) {
        RentingDetailResponseDTO dto = new RentingDetailResponseDTO();
        dto.setRentingDetailId(detail.getRentingDetailId());
        dto.setCarId(detail.getCarId());
        dto.setStartDate(detail.getStartDate());
        dto.setEndDate(detail.getEndDate());
        dto.setPrice(detail.getPrice());
        return dto;
    }
}
