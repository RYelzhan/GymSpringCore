package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.repository.impl.TrainerDAO;
import com.epam.wca.gym.util.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerService extends GenericDAOServiceImpl<Trainer, TrainerDTO, Long> {
    @Autowired
    public TrainerService(TrainerDAO trainerDAO) {
        super(trainerDAO);
    }

    @Override
    public Trainer save(TrainerDTO dto) {
        Trainer trainer = UserFactory.createTrainer(dto);

        genericDAO.save(trainer);

        return trainer;
    }
}
