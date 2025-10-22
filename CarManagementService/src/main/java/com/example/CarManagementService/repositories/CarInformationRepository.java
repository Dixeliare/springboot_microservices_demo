package com.example.CarManagementService.repositories;

import com.example.CarManagementService.models.CarInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarInformationRepository extends JpaRepository<CarInformation, Integer> {
    Optional<CarInformation> findByCarIdAndCarStatus(Integer carId, String carStatus);
}
