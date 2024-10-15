package com.epam.wca.gym.util;

import lombok.experimental.UtilityClass;

import java.util.Base64;

@UtilityClass
public class AuthenticationUtils {
    private static final String AUTHENTICATION_TYPE = "Basic ";
    private static final String SPLIT_TYPE = ":";
    private static final int LIMIT_OF_CREDENTIALS = 3;
    public static String[] extractCredentials(String authHeader) {
        if (authHeader != null && authHeader.startsWith(AUTHENTICATION_TYPE)) {
            String base64Credentials = authHeader.substring(AUTHENTICATION_TYPE.length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            return credentials.split(SPLIT_TYPE, LIMIT_OF_CREDENTIALS);
        }
        throw new IllegalArgumentException("Invalid authentication header");
    }

    public static boolean validatePassword(String rawPassword, String encodedPassword) {
        // TODO: use BCrypt password encoder in the future
        // TODO: Replace this with actual bcrypt check later
        return rawPassword.equals(encodedPassword);
    }
}
