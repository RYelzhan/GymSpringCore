package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.dto.TrainerSavingDTO;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.impl.TrainerDAO;
import com.epam.wca.gym.util.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Service
public class TrainerService extends GenericDAOServiceImpl<Trainer, TrainerSavingDTO, Long> {
    private final TrainerDAO trainerDAO;

    @Autowired
    public TrainerService(TrainerDAO trainerDAO) {
        super(trainerDAO);
        this.trainerDAO = trainerDAO;
    }

    @Override
    public Trainer save(TrainerSavingDTO dto) {
        Trainer trainer = UserFactory.createTrainer(dto);

        trainerDAO.save(trainer);

        return trainer;
    }

    public Set<Training> findAllTrainingsById(long id) {
        return trainerDAO.findAllTrainingsById(id);
    }

    public List<Training> findTrainingByCriteria(String username,
                                                 ZonedDateTime fromDate,
                                                 ZonedDateTime toDate,
                                                 String trainerName,
                                                 TrainingType trainingType) {
        return trainerDAO.findTrainingByCriteria(username,
                fromDate,
                toDate,
                trainerName,
                trainingType);
    }
}
