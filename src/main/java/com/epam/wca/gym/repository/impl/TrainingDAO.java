package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Training;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDAO extends GenericDAOImpl<Training, Long> {
    @Autowired
    public TrainingDAO(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory.createEntityManager(), Training.class);
    }

    @Override
    public Training findByUniqueName(String username) {
        // no guarantee that training name is unique
        return null;
    }
}
