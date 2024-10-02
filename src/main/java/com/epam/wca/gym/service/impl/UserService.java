package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.repository.impl.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService extends GenericDAOServiceImpl<User, User, Long> {
    @Autowired
    public UserService(UserDAO userDAO) {
        super(userDAO);
    }

    @Override
    public User save(User dto) {
        // No use right now
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Long id) {
        // No use right now
        throw new UnsupportedOperationException();
    }

    @Override
    public User findById(Long id) {
        // No use right now
        throw new UnsupportedOperationException();
    }

    @Transactional
    public User findByUniqueNameForAuthentication(String uniqueName) {
        try {
            return genericDAO.findByUniqueName(uniqueName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
