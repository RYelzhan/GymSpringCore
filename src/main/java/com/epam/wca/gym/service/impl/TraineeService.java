package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.impl.TraineeDAO;
import com.epam.wca.gym.util.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Service
public class TraineeService extends GenericDAOServiceImpl<Trainee, TraineeDTO, Long> {
    private final TraineeDAO traineeDAO;
    @Autowired
    public TraineeService(TraineeDAO traineeDAO) {
        super(traineeDAO);
        this.traineeDAO = traineeDAO;
    }

    @Override
    public Trainee save(TraineeDTO dto) {
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
}
