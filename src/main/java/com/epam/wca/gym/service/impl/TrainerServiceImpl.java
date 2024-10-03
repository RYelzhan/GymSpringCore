package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.trainer.TrainerRegistrationDTO;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerUpdateDTO;
import com.epam.wca.gym.dto.training.TrainerTrainingDTO;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.dto.user.UserAuthenticatedDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.repository.TrainerRepository;
import com.epam.wca.gym.service.TrainerService;
import com.epam.wca.gym.service.TrainingTypeService;
import com.epam.wca.gym.util.DTOFactory;
import com.epam.wca.gym.util.Filter;
import com.epam.wca.gym.util.UserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainingTypeService trainingTypeService;

    @Override
    @Transactional
    public UserAuthenticatedDTO save(TrainerRegistrationDTO trainerDTO) {
        var trainingType = trainingTypeService.findByType(trainerDTO.trainingType());

        var trainer = UserFactory.createTrainer(trainerDTO, trainingType);

        trainerRepository.save(trainer);

        return new UserAuthenticatedDTO(trainer.getUserName(), trainer.getPassword());
    }

    @Override
    @Transactional
    public List<TrainingBasicDTO> findTrainingsFiltered(Long id, TrainerTrainingDTO trainerTrainingDTO) {
        var trainer = findById(id);

        return Filter.filterTrainerTrainings(trainer.getTrainings(), trainerTrainingDTO)
                .stream()
                .map(DTOFactory::createTrainerBasicTrainingDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TrainerSendDTO update(Trainer trainer, TrainerUpdateDTO trainerUpdateDTO) {
        var trainingType = trainingTypeService.findByType(trainerUpdateDTO.trainingType());

        trainer.setFirstName(trainerUpdateDTO.firstName());
        trainer.setLastName(trainerUpdateDTO.lastName());
        trainer.setSpecialization(trainingType);
        trainer.setActive(trainerUpdateDTO.isActive());

        // оказываеться finByUserName detaches object
        trainerRepository.save(trainer);

        return DTOFactory.createTrainerSendDTO(trainer);
    }

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
    public Trainer findById(Long id) {
        Optional<Trainer> trainerOptional = trainerRepository.findById(id);

        if (trainerOptional.isPresent()) {
            return trainerOptional.get();
        }

        throw new IllegalArgumentException("No Trainer Found with Id: " + id);
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
}
