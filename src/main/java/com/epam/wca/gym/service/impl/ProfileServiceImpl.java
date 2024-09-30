package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Username;
import com.epam.wca.gym.repository.impl.UsernameDAO;
import com.epam.wca.gym.service.ProfileService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {
    private final UsernameDAO usernameDAO;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;

    @Autowired
    public ProfileServiceImpl(UsernameDAO usernameDAO) {
        this.usernameDAO = usernameDAO;
    }

    @PostConstruct
    public void injectIntoEntities() {
        Trainee.setProfileService(this);
        Trainer.setProfileService(this);
    }

    @Override
    public String createUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        Username username;
        try {
            username = usernameDAO.findByUniqueName(baseUsername);

            username.setCounter(username.getCounter() + 1);

            usernameDAO.update(username);

            return username.getBaseUserName() + username.getCounter();
        } catch (Exception e) {
            log.info("New Base Username Created: " + baseUsername);

            username = new Username(baseUsername, 1L);

            usernameDAO.save(username);

            return baseUsername;
        }
    }

    @Override
    public String createPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
