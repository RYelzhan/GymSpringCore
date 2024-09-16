package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.TrainingType;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeDAO extends GenericDAOImpl<TrainingType, Long> {
    @Autowired
    public TrainingTypeDAO(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory.createEntityManager(), TrainingType.class);
    }

    @Override
    public TrainingType findByUniqueName(String type) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            TrainingType trainingType = (TrainingType) entityManager.createQuery("SELECT t FROM TrainingType t WHERE t.type = ?1")
                    .setParameter(1, type)
                    .getSingleResult();

            entityManager.detach(trainingType);

            transaction.commit();

            return trainingType;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new IllegalArgumentException("Entity was not found");
        }
    }
}
