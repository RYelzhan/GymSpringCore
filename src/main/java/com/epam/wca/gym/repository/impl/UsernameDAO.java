package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Username;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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

            /*
            TODO: Replace above code with:
                TypedQuery<Username> query = entityManager.createQuery("SELECT t FROM Username t WHERE t.baseUserName = :baseUserName", Username.class);
                query.setParameter("baseUserName", baseUserName);
                List<Username> usernames = query.getResultList();
             */

            List<Username> usernames = query.getResultList();

            if (!usernames.isEmpty()) {
                // Optionally detach the entity
                entityManager.detach(usernames.get(0));
                return usernames.get(0);  // Return the first result if found
            } else {
                return null;  // No matching Username found
            }
        } catch (Exception e) {
            return null;
        }
    }
}
