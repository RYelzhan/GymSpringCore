package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.impl.TrainerDAO;
import com.epam.wca.gym.util.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TrainerService extends GenericDAOServiceImpl<Trainer, TrainerDTO, Long> {
    private TrainerDAO trainerDAO;
    @Autowired
    public TrainerService(TrainerDAO trainerDAO) {
        super(trainerDAO);
        this.trainerDAO = trainerDAO;
    }

    @Override
    public Trainer save(TrainerDTO dto) {
        Trainer trainer = UserFactory.createTrainer(dto);

        genericDAO.save(trainer);

        return trainer;
    }

    public Set<Training> findAllTrainingsById(long id) {
        return trainerDAO.findAllTrainingsById(id);
    }
}
