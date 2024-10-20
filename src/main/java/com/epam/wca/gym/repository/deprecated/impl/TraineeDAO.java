package com.epam.wca.gym.repository.deprecated.impl;

import com.epam.wca.gym.entity.Trainee;
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
public class TraineeDAO extends GenericDAOImpl<Trainee, Long> {
    @Autowired
    public TraineeDAO(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory.createEntityManager(),
                Trainee.class);
    }

    @Override
    public Trainee findByUniqueName(String username) {
        try {
            TypedQuery<Trainee> query = entityManager.createQuery(
                    "SELECT t FROM Trainee t WHERE t.userName = :username", Trainee.class);
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

            Trainee trainee = entityManager.find(entityClass, id);
            Set<Training> trainings = trainee.getTrainings();

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
                                                 String trainerName,
                                                 TrainingType trainingType) {
        StringBuilder jpql = new StringBuilder("SELECT t FROM Training t WHERE t.trainee.userName = :username");

        if (fromDate != null) {
            jpql.append(" AND t.trainingDate >= :fromDate");
        }
        if (toDate != null) {
            jpql.append(" AND t.trainingDate <= :toDate");
        }
        if (trainerName != null && !trainerName.isEmpty()) {
            jpql.append(" AND t.trainer.userName = :trainerName");
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
        if (trainerName != null && !trainerName.isEmpty()) {
            query.setParameter("trainerName", trainerName);
        }
        if (trainingType != null) {
            query.setParameter("trainingType", trainingType);
        }

        return query.getResultList();
    }
}
