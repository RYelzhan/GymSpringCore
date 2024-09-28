package com.epam.wca.gym.service;

public interface ProfileService {
    String createUsername(String firstName, String lastName);

    String createUsername(String username);

    String createPassword();
}
