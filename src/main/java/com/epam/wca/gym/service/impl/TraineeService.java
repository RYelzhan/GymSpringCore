package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.trainee.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.trainee.TraineeUpdateDTO;
import com.epam.wca.gym.dto.trainer.TrainerBasicDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.impl.TraineeDAO;
import com.epam.wca.gym.service.ProfileService;
import com.epam.wca.gym.util.DTOFactory;
import com.epam.wca.gym.util.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TraineeService extends GenericDAOServiceImpl<Trainee, TraineeRegistrationDTO, Long> {
    private final TraineeDAO traineeDAO;
    private final ProfileService profileService;
    private final TrainerService trainerService;

    @Autowired
    public TraineeService(TraineeDAO traineeDAO,
                          ProfileService profileService,
                          TrainerService trainerService) {
        super(traineeDAO);
        this.traineeDAO = traineeDAO;
        this.profileService = profileService;
        this.trainerService = trainerService;
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

        trainee.setUserName(profileService.createUsername(traineeUpdateDTO.username()));
        trainee.setFirstName(traineeUpdateDTO.firstName());
        trainee.setLastName(traineeUpdateDTO.lastName());
        trainee.setDateOfBirth(traineeUpdateDTO.dateOfBirth());
        trainee.setAddress(traineeUpdateDTO.address());
        trainee.setActive(traineeUpdateDTO.isActive());

        traineeDAO.update(trainee);

        return traineeDAO.findById(trainee.getId());
    }

    public List<TrainerBasicDTO> getListOfNotAssignedTrainers(Trainee trainee) {
        traineeDAO.update(trainee);

        return getListOfNotAssignedTrainers(trainee.getTrainersAssigned(),
                trainerService.findAll());
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
