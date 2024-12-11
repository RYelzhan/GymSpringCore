package com.epam.wca.gym.service.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.gym.communication.AuthenticationCommunicationService;
import com.epam.wca.gym.communication.StatisticsCommunicationService;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerTrainingCreateDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.exception.ProfileNotFoundException;
import com.epam.wca.gym.repository.TraineeRepository;
import com.epam.wca.gym.repository.TrainerRepository;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.TrainingService;
import com.epam.wca.gym.service.TrainingTypeService;
import com.epam.wca.gym.util.DTOFactory;
import com.epam.wca.gym.util.Filter;
import com.epam.wca.gym.util.TrainingFactory;
import com.epam.wca.gym.util.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeService trainingTypeService;
    private final TrainingService trainingService;
    private final StatisticsCommunicationService statisticsCommunicationService;
    private final AuthenticationCommunicationService authenticationCommunicationService;

    @Override
    public TrainerSendDTO getProfile(Trainer trainer) {
        return DTOFactory.createTrainerSendDTO(trainer);
    }

    @Override
    @Logging
    @Transactional
    public UserAuthenticatedDTO save(TrainerRegistrationDTO trainerDTO) {
        //TODO: Call register of Authentication and get Username/Id

        var trainingType = trainingTypeService.findByType(trainerDTO.trainingType());

        var trainer = UserFactory.createTrainer(trainerDTO, trainingType);

        var authenticatedUser = new UserAuthenticatedDTO(trainer.getUsername(), null);

        trainerRepository.save(trainer);

        return authenticatedUser;
    }

    @Override
    @Logging
    @Transactional
    public List<TrainingBasicDTO> findTrainingsFiltered(Long id, TrainerTrainingQuery trainerTrainingQuery) {
        var trainer = findById(id);

        return Filter.filterTrainerTrainings(trainer.getTrainings(), trainerTrainingQuery)
                .stream()
                .map(DTOFactory::createTrainerBasicTrainingDTO)
                .toList();
    }

    @Override
    @Logging
    @Transactional
    public TrainerSendDTO update(Trainer trainer, TrainerUpdateDTO trainerUpdateDTO) {
        var trainingType = trainingTypeService.findByType(trainerUpdateDTO.trainingType());

        trainer.setFirstname(trainerUpdateDTO.firstName());
        trainer.setLastname(trainerUpdateDTO.lastName());
        trainer.setSpecialization(trainingType);
        trainer.setActive(trainerUpdateDTO.isActive());

        // it turns out finByUserName detaches object
        trainerRepository.save(trainer);

        return DTOFactory.createTrainerSendDTO(trainer);
    }

    @Override
    @Logging
    @Transactional
    public List<TrainerBasicDTO> findActiveUnassignedTrainers(Set<Trainer> assignedTrainers) {
        return trainerRepository.findActiveUnassignedTrainers(assignedTrainers)
                .stream().map(DTOFactory::createBasicTrainerDTO)
                .toList();
    }

    @Override
    @Logging
    public Trainer findById(Long id) {
        Optional<Trainer> trainerOptional = trainerRepository.findById(id);

        if (trainerOptional.isPresent()) {
            return trainerOptional.get();
        }

        throw new ProfileNotFoundException("No Trainer Found with Id: " + id);
    }

    @Override
    @Logging
    @Transactional
    public void deleteById(Long id) {
        deleteAuthAccount();
        deleteAssociatedTrainings(id);

        trainerRepository.deleteById(id);
    }

    private void deleteAuthAccount() {
        authenticationCommunicationService.delete();
    }

    @Override
    public void deleteAssociatedTrainings(Long id) {
        try {
            var trainer = trainerRepository.getReferenceById(id);

            var trainingsDeleteDTO =
                    DTOFactory.createTrainersTrainingsDeleteDTO(trainer.getTrainings());

            log.info("Calling the Statistics service with argument: " + trainingsDeleteDTO);

            statisticsCommunicationService.deleteTrainings(trainingsDeleteDTO);
        } catch (EntityNotFoundException e) {
            throw new InternalErrorException("Deletion of non-existent trainee trainings.");
        }
    }

    @Override
    @Logging
    public Trainer findByUsername(String username) {
        return trainerRepository.findTrainerByUsername(username)
                .orElseThrow(() -> new InternalErrorException(
                        "No Trainer Found with Username: %s".formatted(username)
                ));
    }

    @Override
    @Logging
    @Transactional
    public void createTraining(Trainer trainer, TrainerTrainingCreateDTO trainingDTO) {
        // TODO: replace with service call
        var trainee = traineeRepository.findTraineeByUsername(trainingDTO.traineeUsername());

        if (trainee.isEmpty()) {
            throw new InternalErrorException("No Trainee Found with Username: " + trainingDTO.traineeUsername());
        }

        // trainer might create training without assigning himself to trainee, so, we do it manually
        trainee.get().getTrainersAssigned().add(trainer);

        trainingService.save(TrainingFactory.createTraining(
                trainingDTO,
                trainee.get(), trainer
        ));
    }
}
