package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.repository.TrainerDAO;
import com.epam.wca.gym.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private TrainerDAO trainerDAO;
    public void createTrainer(TrainerDTO trainerDTO) {
        Trainer trainer = new Trainer(trainerDTO.firstName(), trainerDTO.lastName(), trainerDTO.trainingType());
        trainerDAO.save(trainer);
    }

    public void updateTrainerByUsername(String username, Trainer trainer) {
        trainerDAO.updateByUsername(username, trainer);
    }

    public Trainer findByUsername(String username) {
        return trainerDAO.findByUsername(username);
    }
}
