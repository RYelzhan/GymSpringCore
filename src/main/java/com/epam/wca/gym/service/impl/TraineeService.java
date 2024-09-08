package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.TraineeDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.repository.impl.TraineeDAO;
import com.epam.wca.gym.util.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraineeService extends GenericDAOServiceImpl<Trainee, TraineeDTO, Long> {
    @Autowired
    public TraineeService(TraineeDAO traineeDAO) {
        super(traineeDAO);
    }

    @Override
    public Trainee save(TraineeDTO dto) {
        Trainee trainee = UserFactory.createTrainee(dto);

        genericDAO.save(trainee);

        return trainee;
    }
}
