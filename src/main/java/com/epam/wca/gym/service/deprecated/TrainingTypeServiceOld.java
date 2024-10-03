package com.epam.wca.gym.service.deprecated;

import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.deprecated.impl.TrainingTypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingTypeServiceOld extends GenericDAOServiceImpl<TrainingType, TrainingType, Long> {
    @Autowired
    public TrainingTypeServiceOld(TrainingTypeDAO trainingTypeDAO) {
        super(trainingTypeDAO);
    }

    @Override
    public TrainingType save(TrainingType dto) {
        // No use right now
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(TrainingType entity) {
        // No use right now
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Long id) {
        // No use right now
        throw new UnsupportedOperationException();
    }

    @Override
    public TrainingType findById(Long id) {
        // No use right now
        throw new UnsupportedOperationException();
    }
}
