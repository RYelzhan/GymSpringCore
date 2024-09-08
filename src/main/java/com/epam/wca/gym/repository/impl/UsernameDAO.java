package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Username;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
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
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Username username = (Username) entityManager.createQuery("SELECT t FROM Username t WHERE t.baseUserName = ?1")
                    .setParameter(1, baseUsername)
                    .getSingleResult();

            entityManager.detach(username);

            transaction.commit();

            return username;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new IllegalArgumentException("Entity was not found");
        }
    }
}
