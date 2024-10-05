package com.epam.wca.gym.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseMessages {
    public static final String INVALID_INPUT_DESCRIPTION = "Invalid input data." +
            " This error occurs when the input does not meet the validation criteria." +
            " Check the field-level errors for more details.";
    public static final String UNAUTHORIZED_ACCESS_DESCRIPTION = "Unauthorized access." +
            " Authentication failed due to invalid credentials or missing authorization header." +
            " Ensure the username/password are correct and the authorization header is provided.";
}
