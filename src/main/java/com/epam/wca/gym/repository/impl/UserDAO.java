package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO extends GenericDAOImpl<User, Long> {
    @Autowired
    public UserDAO(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory.createEntityManager(), User.class);
    }

    @Override
    public User findByUniqueName(String username) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            User user = (User) entityManager.createQuery("SELECT u FROM User u WHERE u.userName = ?1")
                    .setParameter(1, username)
                    .getSingleResult();

            entityManager.detach(user);

            transaction.commit();

            return user;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new IllegalArgumentException("Entity was not found");
        }
    }
}
