package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerTrainingCreateDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.exception.InternalErrorException;
import com.epam.wca.gym.exception.ProfileNotFoundException;
import com.epam.wca.gym.repository.TraineeRepository;
import com.epam.wca.gym.repository.TrainerRepository;
import com.epam.wca.gym.communication.StatisticsCommunicationService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final StatisticsCommunicationService statisticsCommunicationService;

    @Override
    @Transactional
    public UserAuthenticatedDTO save(TrainerRegistrationDTO trainerDTO) {
        var trainingType = trainingTypeService.findByType(trainerDTO.trainingType());

        var trainer = UserFactory.createTrainer(trainerDTO, trainingType);

        var authenticatedUser = new UserAuthenticatedDTO(trainer.getUsername(), trainer.getPassword());

        trainer.setPassword(passwordEncoder.encode(trainer.getPassword()));

        trainerRepository.save(trainer);

        return authenticatedUser;
    }

    @Override
    @Transactional
    public List<TrainingBasicDTO> findTrainingsFiltered(Long id, TrainerTrainingQuery trainerTrainingQuery) {
        var trainer = findById(id);

        return Filter.filterTrainerTrainings(trainer.getTrainings(), trainerTrainingQuery)
                .stream()
                .map(DTOFactory::createTrainerBasicTrainingDTO)
                .toList();
    }

    @Override
    @Transactional
    public TrainerSendDTO update(Trainer trainer, TrainerUpdateDTO trainerUpdateDTO) {
        var trainingType = trainingTypeService.findByType(trainerUpdateDTO.trainingType());

        trainer.setFirstName(trainerUpdateDTO.firstName());
        trainer.setLastName(trainerUpdateDTO.lastName());
        trainer.setSpecialization(trainingType);
        trainer.setActive(trainerUpdateDTO.isActive());

        // it turns out finByUserName detaches object
        trainerRepository.save(trainer);

        return DTOFactory.createTrainerSendDTO(trainer);
    }

    @Override
    @Transactional
    public List<TrainerBasicDTO> findActiveUnassignedTrainers(Set<Trainer> assignedTrainers) {
        return trainerRepository.findActiveUnassignedTrainers(assignedTrainers)
                .stream().map(DTOFactory::createBasicTrainerDTO)
                .toList();
    }

    @Override
    public Trainer findById(Long id) {
        Optional<Trainer> trainerOptional = trainerRepository.findById(id);

        if (trainerOptional.isPresent()) {
            return trainerOptional.get();
        }

        throw new ProfileNotFoundException("No Trainer Found with Id: " + id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        deleteAssociatedTrainings(id);

        trainerRepository.deleteById(id);
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
    public Trainer findByUsername(String username) {
        return trainerRepository.findTrainerByUserName(username)
                .orElseThrow(() -> new InternalErrorException(
                        "No Trainer Found with Username: %s".formatted(username)
                ));
    }

    @Override
    @Transactional
    public void createTraining(Trainer trainer, TrainerTrainingCreateDTO trainingDTO) {
        // TODO: replace with service call
        var trainee = traineeRepository.findTraineeByUserName(trainingDTO.traineeUsername());

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
