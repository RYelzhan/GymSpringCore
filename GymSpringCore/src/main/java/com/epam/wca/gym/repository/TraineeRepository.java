package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> findTraineeByUsername(String username);
}
