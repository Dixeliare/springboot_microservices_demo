package com.example.CarManagementService.repositories;

import com.example.CarManagementService.models.CarInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarInformationRepository extends JpaRepository<CarInformation, Integer> {
    List<CarInformation> findByCarStatus(String carBrand);
    Optional<CarInformation> findByCarIdAndCarStatus(Integer carId, String carStatus);
}
