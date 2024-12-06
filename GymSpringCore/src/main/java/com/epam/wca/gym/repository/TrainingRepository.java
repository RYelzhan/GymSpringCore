package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
}
