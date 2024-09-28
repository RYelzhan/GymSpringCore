package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.TraineeRegistrationDTO;
import com.epam.wca.gym.dto.TraineeUpdateDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.impl.TraineeDAO;
import com.epam.wca.gym.service.ProfileService;
import com.epam.wca.gym.util.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Service
public class TraineeService extends GenericDAOServiceImpl<Trainee, TraineeRegistrationDTO, Long> {
    private final TraineeDAO traineeDAO;
    private final ProfileService profileService;

    @Autowired
    public TraineeService(TraineeDAO traineeDAO,
                          ProfileService profileService) {
        super(traineeDAO);
        this.traineeDAO = traineeDAO;
        this.profileService = profileService;
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
}
