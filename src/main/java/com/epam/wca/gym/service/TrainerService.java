package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerTrainingCreateDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingDTO;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.Trainer;

import java.util.List;
import java.util.Set;

public interface TrainerService {
    UserAuthenticatedDTO save(TrainerRegistrationDTO trainer);

    List<TrainingBasicDTO> findTrainingsFiltered(Long id, TrainerTrainingDTO trainerTrainingDTO);

    TrainerSendDTO update(Trainer trainer, TrainerUpdateDTO trainerUpdateDTO);

    List<Trainer> findAll();

    List<TrainerBasicDTO> findActiveUnassignedTrainers(Set<Trainer> assignedTrainers);

    Trainer findById(Long id);

    void deleteById(Long id);

    Trainer findByUsername(String username);

    void createTraining(Trainer trainer, TrainerTrainingCreateDTO trainingDTO);
}
