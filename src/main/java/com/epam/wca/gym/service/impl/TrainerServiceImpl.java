package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.repository.TrainerRepository;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.util.DTOFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;

    @Override
    public List<Trainer> findAll() {
        return trainerRepository.findAll();
    }

    @Override
    public List<TrainerBasicDTO> findActiveUnassignedTrainers(Set<Trainer> assignedTrainers) {
        return trainerRepository.findActiveUnassignedTrainers(assignedTrainers)
                .stream().map(DTOFactory::createBasicTrainerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUsername(String username) {
        return trainerRepository.existsByUserName(username);
    }

    @Override
    public Trainer findByUsername(String username) {
        return trainerRepository.findTrainerByUserName(username);
    }
}
