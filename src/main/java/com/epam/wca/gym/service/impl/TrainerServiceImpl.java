package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerTrainingCreateDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingQuery;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.exception.ControllerValidationException;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeService trainingTypeService;
    private final TrainingService trainingService;

    @Override
    @Transactional
    public UserAuthenticatedDTO save(TrainerRegistrationDTO trainerDTO) {
        var trainingType = trainingTypeService.findByType(trainerDTO.trainingType());

        var trainer = UserFactory.createTrainer(trainerDTO, trainingType);

        trainerRepository.save(trainer);

        // TODO: find a way to return raw password and save an encoded one
        return new UserAuthenticatedDTO(trainer.getUsername(), trainer.getPassword());
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
        trainerRepository.deleteById(id);
    }

    @Override
    public Trainer findByUsername(String username) {
        return trainerRepository.findTrainerByUserName(username);
    }

    @Override
    @Transactional
    public void createTraining(Trainer trainer, TrainerTrainingCreateDTO trainingDTO) {
        Trainee trainee = traineeRepository.findTraineeByUserName(trainingDTO.traineeUsername());

        if (trainee == null) {
            throw new ControllerValidationException("No Trainee Found with Username: " + trainingDTO.traineeUsername());
        }

        // trainer might create training without assigning himself to trainee, so, we do it manually
        trainee.getTrainersAssigned().add(trainer);

        trainingService.save(TrainingFactory.createTraining(
                trainingDTO,
                trainee, trainer
        ));
    }
}
