package com.epam.wca.statistics.repository;

import com.epam.wca.statistics.entity.TrainerWorkload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerWorkloadRepository extends JpaRepository<TrainerWorkload, Long> {
    Optional<TrainerWorkload> findByUsernameAndYearAndMonth(String username, Integer year, Integer month);

    List<TrainerWorkload> findAllByUsername(String username);
}
