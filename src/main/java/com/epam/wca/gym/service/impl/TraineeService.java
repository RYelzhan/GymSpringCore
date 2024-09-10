package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.impl.TraineeDAO;
import com.epam.wca.gym.util.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TraineeService extends GenericDAOServiceImpl<Trainee, TraineeDTO, Long> {
    private TraineeDAO traineeDAO;
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
}
