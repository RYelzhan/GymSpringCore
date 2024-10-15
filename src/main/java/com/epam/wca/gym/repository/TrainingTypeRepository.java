package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    TrainingType findTrainingTypeByType(String type);
}
