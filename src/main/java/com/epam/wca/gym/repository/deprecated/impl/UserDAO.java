package com.epam.wca.gym.repository.deprecated.impl;

import com.epam.wca.gym.entity.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDAO extends GenericDAOImpl<User, Long> {
    @Autowired
    public UserDAO(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory.createEntityManager(), User.class);
    }

    @Override
    @Transactional
    public User findByUniqueName(String username) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT t FROM User t WHERE t.userName = :username", User.class);
            query.setParameter("username", username);

            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
