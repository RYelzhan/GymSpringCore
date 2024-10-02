package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.TrainingType;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
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
        try {
            TypedQuery<TrainingType> query = entityManager.createQuery(
                    "SELECT t FROM TrainingType t WHERE t.type = :type", TrainingType.class);
            query.setParameter("type", type);

            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
