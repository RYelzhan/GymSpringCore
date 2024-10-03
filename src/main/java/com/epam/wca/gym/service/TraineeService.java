package com.epam.wca.gym.service;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainersUpdateDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.training.TraineeTrainingDTO;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.Trainee;

import java.util.List;

public interface TraineeService {
    UserAuthenticatedDTO save(TraineeRegistrationDTO trainee);

    List<TrainingBasicDTO> findTrainingsFiltered(Long id, TraineeTrainingDTO traineeTrainingDTO);

    TraineeSendDTO update(Trainee trainee, TraineeUpdateDTO traineeUpdateDTO);

    List<TrainerBasicDTO> getListOfNotAssignedTrainers(Trainee trainee);

    void deleteById(Long id);

    List<TrainerBasicDTO> addTrainers(Trainee trainee, TraineeTrainersUpdateDTO dto);

    Trainee findById(Long id);
}
