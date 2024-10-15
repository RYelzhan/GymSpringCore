package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.entity.Username;
import com.epam.wca.gym.repository.UsernameRepository;
import com.epam.wca.gym.service.ProfileService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Setter
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UsernameRepository usernameRepository;
    @Value("${gym.api.profile.password.characters}")
    private String characters;
    @Value("${gym.api.profile.password.length}")
    private int passwordLength;

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
            username = usernameRepository.findUsernameByBaseUserName(baseUsername);

            username.setCounter(username.getCounter() + 1);

            // because username gets detached
            usernameRepository.save(username);

            return username.getBaseUserName() + username.getCounter();
        } catch (Exception e) {
            log.info("New Base Username Created: " + baseUsername);

            username = new Username(baseUsername, 1L);

            usernameRepository.save(username);

            return baseUsername;
        }
    }

    @Override
    public String createPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(passwordLength);

        for (int i = 0; i < passwordLength; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }
}
