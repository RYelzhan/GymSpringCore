package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.repository.impl.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends GenericDAOServiceImpl<User, User, Long> {
    @Autowired
    public UserService(UserDAO userDAO) {
        super(userDAO);
    }

    @Override
    public User save(User dto) {
        // can not save just user right now
        return null;
    }
}
