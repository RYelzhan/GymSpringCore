package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.TrainerDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.repository.TrainerDAO;
import com.epam.wca.gym.service.TrainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private TrainerDAO trainerDAO;
    public Trainer create(TrainerDTO trainerDTO) {
        Trainer newTrainer = trainerDAO.save(new Trainer(trainerDTO.firstName(), trainerDTO.lastName(), trainerDTO.trainingType()));

        log.info("Creating New Trainer: " + newTrainer.getUserName());

        return newTrainer;
    }

    public void updateByUsername(String username, Trainer trainer) {
        trainerDAO.updateByUsername(username, trainer);
    }

    public Trainer findByUsername(String username) {
        return trainerDAO.findByUsername(username);
    }

    public Trainer findById(long id) {
        return trainerDAO.findById(id);
    }
}
