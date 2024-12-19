package com.epam.wca.statistics.repository;

import com.epam.wca.statistics.entity.TrainerTrainingSummary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TrainerTrainingSummaryRepository extends MongoRepository<TrainerTrainingSummary, String> {
    Optional<TrainerTrainingSummary> findTrainerTrainingSummaryByUsername(String username);
}
