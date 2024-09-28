package com.epam.wca.gym.util;

import com.epam.wca.gym.dto.EmbeddedTraineeDTO;
import com.epam.wca.gym.dto.EmbeddedTrainerDTO;
import com.epam.wca.gym.dto.TraineeSendDTO;
import com.epam.wca.gym.dto.TrainerSendDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;

import java.util.HashSet;
import java.util.Set;

public class DTOFactory {

    public static TraineeSendDTO createTraineeSendDTO(Trainee trainee) {
        return new TraineeSendDTO(trainee.getFirstName(),
                trainee.getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.isActive(),
                createEmbeddedTrainerDTOSet(trainee.getTrainersAssigned()));
    }

    public static Set<EmbeddedTrainerDTO> createEmbeddedTrainerDTOSet(Set<Trainer> trainerSet) {
        Set<EmbeddedTrainerDTO> result = new HashSet<>();

        trainerSet.forEach(trainer -> {
            result.add(createEmbeddedTrainerDTO(trainer));
        });

        return result;
    }

    public static EmbeddedTrainerDTO createEmbeddedTrainerDTO(Trainer trainer) {
        return new EmbeddedTrainerDTO(trainer.getUserName(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getSpecialization().toString());
    }

    public static TrainerSendDTO createTrainerSendDTO(Trainer trainer) {
        return new TrainerSendDTO(trainer.getUserName(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getSpecialization().toString(),
                trainer.isActive(),
                createEmbeddedTraineeDTOSet(trainer.getTraineesAssigned()));
    }

    public static Set<EmbeddedTraineeDTO> createEmbeddedTraineeDTOSet(Set<Trainee> traineeSet) {
        Set<EmbeddedTraineeDTO> result = new HashSet<>();

        traineeSet.forEach(trainee -> {
            result.add(createEmbeddedTraineeDTO(trainee));
        });

        return result;
    }

    public static EmbeddedTraineeDTO createEmbeddedTraineeDTO(Trainee trainee) {
        return new EmbeddedTraineeDTO(trainee.getUserName(),
                trainee.getFirstName(),
                trainee.getLastName());
    }
}
