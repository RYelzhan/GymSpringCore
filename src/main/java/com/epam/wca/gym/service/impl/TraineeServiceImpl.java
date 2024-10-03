package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainersUpdateDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.training.TraineeTrainingDTO;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.exception.ForbiddenActionException;
import com.epam.wca.gym.repository.TraineeRepository;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.util.DTOFactory;
import com.epam.wca.gym.util.Filter;
import com.epam.wca.gym.util.UserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final TrainerService trainerService;

    @Override
    @Transactional
    public UserAuthenticatedDTO save(TraineeRegistrationDTO dto) {
        var trainee = UserFactory.createTrainee(dto);

        traineeRepository.save(trainee);

        return new UserAuthenticatedDTO(trainee.getUserName(), trainee.getPassword());
    }

    @Override
    @Transactional
    public List<TrainingBasicDTO> findTrainingsFiltered(Long id, TraineeTrainingDTO traineeTrainingDTO) {
        var trainee = findById(id);

        return Filter.filterTraineeTrainings(trainee.getTrainings(), traineeTrainingDTO)
                .stream()
                .map(DTOFactory::createTraineeBasicTrainingDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TraineeSendDTO update(Trainee trainee, TraineeUpdateDTO traineeUpdateDTO) {
        trainee.setFirstName(traineeUpdateDTO.firstName());
        trainee.setLastName(traineeUpdateDTO.lastName());
        trainee.setDateOfBirth(traineeUpdateDTO.dateOfBirth());
        trainee.setAddress(traineeUpdateDTO.address());
        trainee.setActive(traineeUpdateDTO.isActive());

        // оказываеться finByUserName detaches object
        traineeRepository.save(trainee);

        return DTOFactory.createTraineeSendDTO(trainee);
    }

    @Override
    public List<TrainerBasicDTO> getListOfNotAssignedTrainers(Trainee trainee) {
        return trainerService.findActiveUnassignedTrainers(trainee.getTrainersAssigned());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        traineeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<TrainerBasicDTO> addTrainers(Trainee trainee, TraineeTrainersUpdateDTO dto) {
        List<Trainer> addedTrainers = new ArrayList<>();

        dto.trainerUsernames()
                .forEach(username -> {
                    Trainer trainer = trainerService.findByUsername(username);

                    if (trainer == null) {
                        throw new ForbiddenActionException("No Trainer Found with Username: " +
                                username +
                                ". No Trainers Added");
                    }

                    addedTrainers.add(trainer);
                });

        trainee.addTrainers(addedTrainers);

        // оказываеться finByUserName detaches object
        traineeRepository.save(trainee);

        return addedTrainers.stream()
                .map(DTOFactory::createBasicTrainerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Trainee findById(Long id) {
        Optional<Trainee> traineeOptional = traineeRepository.findById(id);

        if (traineeOptional.isPresent()) {
            return traineeOptional.get();
        }

        throw new IllegalArgumentException("No Trainee Found with Id: " + id);
    }
}
