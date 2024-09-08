package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.TrainingType;
import com.epam.wca.gym.repository.impl.TrainingTypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingTypeService extends GenericDAOServiceImpl<TrainingType, TrainingType, Long> {
    @Autowired
    public TrainingTypeService(TrainingTypeDAO trainingTypeDAO) {
        super(trainingTypeDAO);
    }

    @Override
    public TrainingType save(TrainingType dto) {
        // used for set-up
        genericDAO.save(dto);
        // No use right now
        return null;
    }
}
