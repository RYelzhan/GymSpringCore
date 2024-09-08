package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Trainee;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDAO extends GenericDAOImpl<Trainee, Long> {
    @Autowired
    public TraineeDAO(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory.createEntityManager(), Trainee.class);
    }

    @Override
    public Trainee findByUniqueName(String username) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Trainee trainee = (Trainee) entityManager.createQuery("SELECT t FROM Trainee t WHERE t.userName = ?1")
                    .setParameter(1, username)
                    .getSingleResult();

            entityManager.detach(trainee);

            transaction.commit();

            return trainee;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new IllegalArgumentException("Entity was not found");
        }
    }
}
