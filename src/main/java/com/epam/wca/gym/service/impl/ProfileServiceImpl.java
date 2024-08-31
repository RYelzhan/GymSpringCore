package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Map;
@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private Map<String, Integer> usernameCounter;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;
    @PostConstruct
    public void injectIntoEntities() {
        Trainee.setProfileService(this);
        Trainer.setProfileService(this);
    }

    public String createUserName(String firstName, String lastName) {
        String baseUsername = firstName + '.' + lastName;
        log.info("Creating Username: " + baseUsername);

        String userName = baseUsername;

        if (usernameCounter.containsKey(baseUsername)) {
            int count = usernameCounter.get(baseUsername) + 1;

            userName = baseUsername + count;

            usernameCounter.put(baseUsername, count);
        } else {
            usernameCounter.put(baseUsername, 1);
        }

        return userName;
    }

    public String createPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

    public Map<String, Integer> getUsernameCounter() {
        return usernameCounter;
    }
}
