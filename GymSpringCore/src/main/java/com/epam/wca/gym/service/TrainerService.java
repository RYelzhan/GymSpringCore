package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerTrainingCreateDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.common.gymcommon.auth_dto.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.Trainer;

import java.util.List;
import java.util.Set;

public interface TrainerService {
    TrainerSendDTO getProfile(Trainer trainer);

    UserAuthenticatedDTO save(TrainerRegistrationDTO trainer);

    List<TrainingBasicDTO> findTrainingsFiltered(Long id, TrainerTrainingQuery trainerTrainingQuery);

    TrainerSendDTO update(Trainer trainer, TrainerUpdateDTO trainerUpdateDTO);

    List<TrainerBasicDTO> findActiveUnassignedTrainers(Set<Trainer> assignedTrainers);

    Trainer findById(Long id);

    void deleteAssociatedTrainings(Long id);

    void deleteById(Long id);

    Trainer findByUsername(String username);

    void createTraining(Trainer trainer, TrainerTrainingCreateDTO trainingDTO);
}
