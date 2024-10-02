package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Username;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsernameDAO extends GenericDAOImpl<Username, Long> {

    @Autowired
    public UsernameDAO(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory.createEntityManager(), Username.class);
    }

    @Override
    public Username findByUniqueName(String baseUsername) {
        try {
            TypedQuery<Username> query = entityManager.createQuery(
                    "SELECT t FROM Username t WHERE t.baseUserName = :baseUserName", Username.class);
            query.setParameter("baseUserName", baseUsername);

            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
