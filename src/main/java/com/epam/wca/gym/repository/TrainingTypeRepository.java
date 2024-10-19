package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    Optional<TrainingType> findTrainingTypeByType(String type);
}
