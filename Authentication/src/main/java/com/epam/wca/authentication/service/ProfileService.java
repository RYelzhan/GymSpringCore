package com.epam.wca.authentication.service;

public interface ProfileService {
    String createUsername(String firstName, String lastName);

    String createPassword();
}
