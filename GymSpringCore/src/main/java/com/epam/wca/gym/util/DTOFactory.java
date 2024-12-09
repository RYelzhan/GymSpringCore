package com.epam.wca.gym.util;

import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.gym.dto.trainee.TraineeBasicDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.dto.trainee.TraineeSendDTO;
import com.epam.wca.gym.dto.trainer.TrainerSendDTO;
import com.epam.wca.gym.dto.training.TrainingBasicDTO;
import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import lombok.experimental.UtilityClass;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class DTOFactory {

    public static TraineeSendDTO createTraineeSendDTO(Trainee trainee) {
        return new TraineeSendDTO(
                trainee.getFirstname(),
                trainee.getLastname(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.isActive(),
                createBasicTrainerDTOSet(trainee.getTrainersAssigned())
        );
    }

    public static Set<TrainerBasicDTO> createBasicTrainerDTOSet(Set<Trainer> trainerSet) {
        Set<TrainerBasicDTO> result = new HashSet<>();

        trainerSet.forEach(trainer -> result.add(createBasicTrainerDTO(trainer)));

        return result;
    }

    public static TrainerBasicDTO createBasicTrainerDTO(Trainer trainer) {
        return new TrainerBasicDTO(
                trainer.getUsername(),
                trainer.getFirstname(),
                trainer.getLastname(),
                trainer.getSpecialization().toString()
        );
    }

    public static TrainerSendDTO createTrainerSendDTO(Trainer trainer) {
        return new TrainerSendDTO(
                trainer.getUsername(),
                trainer.getFirstname(),
                trainer.getLastname(),
                trainer.getSpecialization().toString(),
                trainer.isActive(),
                createBasicTraineeDTOSet(trainer.getTraineesAssigned())
        );
    }

    public static Set<TraineeBasicDTO> createBasicTraineeDTOSet(Set<Trainee> traineeSet) {
        Set<TraineeBasicDTO> result = new HashSet<>();

        traineeSet.forEach(trainee -> result.add(createBasicTraineeDTO(trainee)));

        return result;
    }

    public static TraineeBasicDTO createBasicTraineeDTO(Trainee trainee) {
        return new TraineeBasicDTO(
                trainee.getUsername(),
                trainee.getFirstname(),
                trainee.getLastname()
        );
    }

    public static TrainingBasicDTO createTraineeBasicTrainingDTO(Training training) {
        return new TrainingBasicDTO(
                training.getTrainingName(),
                training.getTrainingDate(),
                training.getTrainingType().toString(),
                training.getTrainingDuration(),
                training.getTrainer().getUsername()
        );
    }

    public static TrainingBasicDTO createTrainerBasicTrainingDTO(Training training) {
        return new TrainingBasicDTO(
                training.getTrainingName(),
                training.getTrainingDate(),
                training.getTrainingType().toString(),
                training.getTrainingDuration(),
                training.getTrainee().getUsername()
        );
    }

    public static TrainingTypeBasicDTO createBasicTrainingTypeDTO(TrainingType trainingType) {
        return new TrainingTypeBasicDTO(
                trainingType.getType(),
                trainingType.getId()
        );
    }

    public static TrainersTrainingsDeleteDTO createTrainersTrainingsDeleteDTO(Set<Training> trainings) {
        Map<String, Map<ZonedDateTime, Integer>> trainingsInfo = new HashMap<>();

        ZonedDateTime now = ZonedDateTime.now();

        trainings.stream()
                .filter(training -> training.getTrainingDate().isAfter(now))
                .forEach(training ->
                    trainingsInfo.computeIfAbsent(training.getTrainer().getUsername(), k -> new HashMap<>())
                        .merge(training.getTrainingDate(), training.getTrainingDuration(), Integer::sum)
        );

        return new TrainersTrainingsDeleteDTO(trainingsInfo);
    }

    public static TrainerTrainingAddDTO createTrainerTrainingAddDTO(Training training) {
        Trainer trainer = training.getTrainer();

        return new TrainerTrainingAddDTO(
                trainer.getUsername(),
                trainer.getFirstname(),
                trainer.getLastname(),
                trainer.isActive(),
                training.getTrainingDate(),
                training.getTrainingDuration()
        );
    }
}
