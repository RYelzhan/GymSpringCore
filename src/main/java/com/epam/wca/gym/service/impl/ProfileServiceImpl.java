package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {
    private Map<String, Integer> usernameCounter;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;
    @Autowired
    public void setMap(Map<String, Integer> usernameCounter) {
        this.usernameCounter = usernameCounter;
    }

    public String createUserName(String firstName, String lastName) {
        log.info("Creating Username: " + firstName + lastName);
        String baseUsername = firstName + '.' + lastName;
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