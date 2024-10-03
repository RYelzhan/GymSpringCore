package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    @Query("SELECT t FROM Trainer t WHERE t.isActive = true AND t NOT IN (:assignedTrainers)")
    List<Trainer> findActiveUnassignedTrainers(Set<Trainer> assignedTrainers);

    Boolean existsByUserName(String username);

    Trainer findTrainerByUserName(String username);
}
