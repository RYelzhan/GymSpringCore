package com.epam.wca.authentication.service.impl;

import com.epam.wca.authentication.entity.Username;
import com.epam.wca.authentication.repository.UsernameRepository;
import com.epam.wca.authentication.service.ProfileService;
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

    @Override
    public String createUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        var usernameHolder = usernameRepository.findUsernameByBaseUserName(baseUsername);
        if (usernameHolder.isEmpty()) {
            log.info("New Base Username Created: " + baseUsername);

            Username username = new Username(baseUsername, 1L);

            usernameRepository.save(username);

            return baseUsername;
        }
        Username username = usernameHolder.get();

        username.setCounter(username.getCounter() + 1);

        usernameRepository.save(username);

        String constructedUsername = username.getBaseUserName() + username.getCounter();

        usernameRepository.save(new Username(constructedUsername, 1L));

        return constructedUsername;
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
