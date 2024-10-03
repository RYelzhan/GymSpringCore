package com.epam.wca.gym.repository.deprecated.impl;

import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.entity.TrainingType;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Repository
public class TrainerDAO extends GenericDAOImpl<Trainer, Long> {
    @Autowired
    public TrainerDAO(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory.createEntityManager(),
                Trainer.class);
    }

    @Override
    public Trainer findByUniqueName(String username) {
        try {
            TypedQuery<Trainer> query = entityManager.createQuery(
                    "SELECT t FROM Trainer t WHERE t.userName = :username", Trainer.class);
            query.setParameter("username", username);

            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Set<Training> findAllTrainingsById(long id) {
        // right now id is not necessary, but in future it may be
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Trainer trainer = entityManager.find(entityClass, id);
            Set<Training> trainings = trainer.getTrainings();

            transaction.commit();

            return trainings;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new IllegalArgumentException("Entity was not found");
        }
    }

    public List<Training> findTrainingByCriteria(String username,
                                                 ZonedDateTime fromDate,
                                                 ZonedDateTime toDate,
                                                 String traineeName,
                                                 TrainingType trainingType) {
        StringBuilder jpql = new StringBuilder("SELECT t FROM Training t WHERE t.trainer.userName = :username");

        if (fromDate != null) {
            jpql.append(" AND t.trainingDate >= :fromDate");
        }
        if (toDate != null) {
            jpql.append(" AND t.trainingDate <= :toDate");
        }
        if (traineeName != null && !traineeName.isEmpty()) {
            jpql.append(" AND t.trainee.userName = :traineeName");
        }
        if (trainingType != null) {
            jpql.append(" AND t.trainingType = :trainingType");
        }

        TypedQuery<Training> query = entityManager.createQuery(jpql.toString(), Training.class);
        query.setParameter("username", username);

        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }
        if (traineeName != null && !traineeName.isEmpty()) {
            query.setParameter("traineeName", traineeName);
        }
        if (trainingType != null) {
            query.setParameter("trainingType", trainingType);
        }

        return query.getResultList();
    }
}
