package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.facade.user.UserSession;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class TrainerDAO extends GenericDAOImpl<Trainer, Long> {
    private final UserSession userSession;
    @Autowired
    public TrainerDAO(EntityManagerFactory entityManagerFactory,
                      UserSession userSession) {
        super(entityManagerFactory.createEntityManager(),
                Trainer.class);
        this.userSession = userSession;
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

    public Set<Training> findAllTrainingsById(long id) {
        // right now id is not necessary, but in future it may be
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Trainer trainer = entityManager.find(entityClass, id);
            Set<Training> trainings = trainer.getTrainings();
            userSession.setUser(trainer);

            transaction.commit();

            return trainings;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new IllegalArgumentException("Entity was not found");
        }
    }
}
