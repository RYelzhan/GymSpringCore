package com.epam.wca.gym.service.in_memory_impl;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.service.ProfileService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Map;

/**
 * Implementation of {@link ProfileService} that provides user profile management functionalities,
 * including username and password generation.
 *
 * <p>This service is responsible for creating unique usernames and generating random passwords for users.</p>
 *
 * @deprecated This class is deprecated and will be removed in future versions. It is recommended to use
 * an updated implementation that supports additional security features and integrates with the new database
 * architecture. Future updates will include enhanced password hashing and a more scalable username management
 * system.
 * @since 1.1
 * @see ProfileService
 */

@Deprecated(since = "1.1", forRemoval = false)

@Getter
@Slf4j
public class ProfileServiceImpl implements ProfileService {
//    @Autowired
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

        return password.toString(); // Hashing, maybe
    }
}
