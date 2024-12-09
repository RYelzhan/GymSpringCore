package com.epam.wca.gym.service.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.gym.communication.StatisticsCommunicationService;
import com.epam.wca.gym.communication.feign.AuthenticationFeign;
import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainersUpdateDTO;
import com.epam.wca.gym.dto.trainee.TraineeTrainingCreateDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.training.TraineeTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.exception.ProfileNotFoundException;
import com.epam.wca.gym.repository.TraineeRepository;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.TrainingService;
import com.epam.wca.gym.util.DTOFactory;
import com.epam.wca.gym.util.Filter;
import com.epam.wca.gym.util.TrainingFactory;
import com.epam.wca.gym.util.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final StatisticsCommunicationService statisticsCommunicationService;
    private final AuthenticationFeign authenticationFeign;

    @Override
    @Logging
    @Transactional
    public UserAuthenticatedDTO save(TraineeRegistrationDTO dto) {
        //TODO: Call register of Authentication and get Username/Id

        var trainee = UserFactory.createTrainee(dto);

        var authenticatedUser = new UserAuthenticatedDTO(trainee.getUsername(), null);

        traineeRepository.save(trainee);

        return authenticatedUser;
    }

    @Override
    @Logging
    @Transactional
    public List<TrainingBasicDTO> findTrainingsFiltered(Long id, TraineeTrainingQuery traineeTrainingQuery) {
        var trainee = findById(id);

        return Filter.filterTraineeTrainings(trainee.getTrainings(), traineeTrainingQuery)
                .stream()
                .map(DTOFactory::createTraineeBasicTrainingDTO)
                .toList();
    }

    @Override
    @Logging
    @Transactional
    public TraineeSendDTO update(Trainee trainee, TraineeUpdateDTO traineeUpdateDTO) {
        trainee.setFirstname(traineeUpdateDTO.firstName());
        trainee.setLastname(traineeUpdateDTO.lastName());
        if (traineeUpdateDTO.dateOfBirth() != null) {
            trainee.setDateOfBirth(traineeUpdateDTO.dateOfBirth());
        }
        if (traineeUpdateDTO.address() != null) {
            trainee.setAddress(traineeUpdateDTO.address());
        }
        trainee.setActive(traineeUpdateDTO.isActive());

        // it turns out finByUserName detaches object
        traineeRepository.save(trainee);

        return DTOFactory.createTraineeSendDTO(trainee);
    }

    @Override
    @Logging
    @Transactional
    public List<TrainerBasicDTO> getListOfNotAssignedTrainers(Trainee trainee) {
        return trainerService.findActiveUnassignedTrainers(trainee.getTrainersAssigned());
    }

    @Override
    @Logging
    @Transactional
    public void deleteById(Long id) {
        deleteAuthAccount();
        deleteAssociatedTrainings(id);

        traineeRepository.deleteById(id);
    }

    @Logging
    private void deleteAuthAccount() {
        var request =
                    ((ServletRequestAttributes)RequestContextHolder
                            .getRequestAttributes())
                            .getRequest();

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        log.info("Auth Header Sent to Auth Service: {}", authHeader);

        authenticationFeign.delete(authHeader);
    }

    @Override
    @Logging
    public void deleteAssociatedTrainings(Long id) {
        try {
            var trainee = traineeRepository.getReferenceById(id);

            var trainingsDeleteDTO =
                    DTOFactory.createTrainersTrainingsDeleteDTO(trainee.getTrainings());

            log.info("Calling the Statistics service with argument: " + trainingsDeleteDTO);

            statisticsCommunicationService.deleteTrainings(trainingsDeleteDTO);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException("Deletion of non-existent trainee trainings.");
        }
    }

    @Override
    @Logging
    @Transactional
    public List<TrainerBasicDTO> addTrainers(Trainee trainee, TraineeTrainersUpdateDTO dto) {
        List<Trainer> addedTrainers = new ArrayList<>();

        dto.trainerUsernames()
                .forEach(username -> {
                    Trainer trainer = trainerService.findByUsername(username);

                    if (trainer == null) {
                        throw new InternalErrorException(
                                "No Trainer Found with Username: %s. No Trainers Added"
                                        .formatted(username)
                        );
                    }

                    addedTrainers.add(trainer);
                });

        trainee.addTrainers(addedTrainers);

        // it turns out finByUserName detaches object
        traineeRepository.save(trainee);

        return addedTrainers.stream()
                .map(DTOFactory::createBasicTrainerDTO)
                .toList();
    }

    @Override
    @Logging
    public Trainee findById(Long id) {
        Optional<Trainee> traineeOptional = traineeRepository.findById(id);

        if (traineeOptional.isPresent()) {
            return traineeOptional.get();
        }

        throw new ProfileNotFoundException("No Trainee Found with Id: " + id);
    }

    @Override
    @Logging
    @Transactional
    public void createTraining(Trainee trainee, TraineeTrainingCreateDTO trainingDTO) {
        var trainer = trainerService.findByUsername(trainingDTO.trainerUsername());

        trainingService.save(TrainingFactory.createTraining(
                trainingDTO,
                trainee, trainer
        ));
    }
}
