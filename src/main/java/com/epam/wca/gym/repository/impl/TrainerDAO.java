package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Trainer;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDAO extends GenericDAOImpl<Trainer, Long> {
    @Autowired
    public TrainerDAO(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory.createEntityManager(), Trainer.class);
    }

    @Override
    public Trainer findByUniqueName(String username) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Trainer trainer = (Trainer) entityManager.createQuery("SELECT t FROM Trainer t WHERE t.userName = ?1")
                    .setParameter(1, username)
                    .getSingleResult();

            entityManager.detach(trainer);

            transaction.commit();

            return trainer;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new IllegalArgumentException("Entity was not found");
        }
    }
}
