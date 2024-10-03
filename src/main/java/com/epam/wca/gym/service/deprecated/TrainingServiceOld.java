package com.epam.wca.gym.service.deprecated;

import com.epam.wca.gym.dto.TrainingDTO;
import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.deprecated.impl.TraineeDAO;
import com.epam.wca.gym.repository.deprecated.impl.TrainerDAO;
import com.epam.wca.gym.repository.deprecated.impl.TrainingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingServiceOld extends GenericDAOServiceImpl<Training, TrainingDTO, Long> {
    private final TrainingDAO trainingDAO;
    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;

    @Autowired
    public TrainingServiceOld(TrainingDAO trainingDAO, TraineeDAO traineeDAO, TrainerDAO trainerDAO) {
        super(trainingDAO);
        this.trainingDAO = trainingDAO;
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
    }

    @Override
    public Training save(TrainingDTO dto) {
        Trainee trainee = traineeDAO.findById(dto.traineeId());
        Trainer trainer = trainerDAO.findById(dto.trainerId());

        Training training = new Training(trainee,
                trainer,
                dto.trainingName(),
                dto.trainingType(),
                dto.trainingDate(),
                dto.trainingDuration());

        trainingDAO.save(training);

        return training;
    }

    public Training save(Training training) {
        trainingDAO.save(training);

        return training;
    }

    @Override
    public void update(Training entity) {
        // No use right now
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Long id) {
        // No use right now
        throw new UnsupportedOperationException();
    }

    @Override
    public Training findByUniqueName(String uniqueName) {
        // No use right now
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Training> findAll() {
        // No use right now
        throw new UnsupportedOperationException();
    }
}
