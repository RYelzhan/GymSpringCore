package com.epam.wca.gym.service.deprecated;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.deprecated.impl.TraineeDAO;
import com.epam.wca.gym.util.DTOFactory;
import com.epam.wca.gym.util.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TraineeServiceOld extends GenericDAOServiceImpl<Trainee, TraineeRegistrationDTO, Long> {
    private final TraineeDAO traineeDAO;
    private final TrainerServiceOld trainerServiceOld;

    @Autowired
    public TraineeServiceOld(TraineeDAO traineeDAO,
                             TrainerServiceOld trainerServiceOld) {
        super(traineeDAO);
        this.traineeDAO = traineeDAO;
        this.trainerServiceOld = trainerServiceOld;
    }

    @Override
    public Trainee save(TraineeRegistrationDTO dto) {
        Trainee trainee = UserFactory.createTrainee(dto);

        traineeDAO.save(trainee);

        return trainee;
    }

    public Set<Training> findAllTrainingsById(long id) {
        return traineeDAO.findAllTrainingsById(id);
    }

    public List<Training> findTrainingByCriteria(String username,
                                                 ZonedDateTime fromDate,
                                                 ZonedDateTime toDate,
                                                 String trainerName,
                                                 TrainingType trainingType) {
        return traineeDAO.findTrainingByCriteria(username,
                fromDate,
                toDate,
                trainerName,
                trainingType);
    }

    public void deleteByUsername(String username) {
        // I do not see need right now, as program is configured.
        // But for галочка I let it stay here.
        traineeDAO.deleteById(traineeDAO.findByUniqueName(username).getId());
    }

    public Trainee update(Trainee trainee,
                          TraineeUpdateDTO traineeUpdateDTO) {

        trainee.setFirstName(traineeUpdateDTO.firstName());
        trainee.setLastName(traineeUpdateDTO.lastName());
        trainee.setDateOfBirth(traineeUpdateDTO.dateOfBirth());
        trainee.setAddress(traineeUpdateDTO.address());
        trainee.setActive(traineeUpdateDTO.isActive());

        traineeDAO.update(trainee);

        return trainee;
    }

    public List<TrainerBasicDTO> getListOfNotAssignedTrainers(Trainee trainee) {
        return getListOfNotAssignedTrainers(trainee.getTrainersAssigned(),
                trainerServiceOld.findAll());
    }

    private List<TrainerBasicDTO> getListOfNotAssignedTrainers(Set<Trainer> assignedTrainers,
                                                               List<Trainer> allTrainers) {

        List<TrainerBasicDTO> listOfTrainers = new ArrayList<>();

        for (Trainer trainer : allTrainers) {
            if (!assignedTrainers.contains(trainer) && trainer.isActive()) {
                listOfTrainers.add(DTOFactory.createBasicTrainerDTO(trainer));
            }
        }

        return listOfTrainers;
    }
}
