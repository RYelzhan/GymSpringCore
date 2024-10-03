package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.entity.Trainer;

import java.util.List;
import java.util.Set;

public interface TrainerService {
    List<Trainer> findAll();

    List<TrainerBasicDTO> findActiveUnassignedTrainers(Set<Trainer> assignedTrainers);

    boolean existsByUsername(String username);

    Trainer findByUsername(String username);
}
