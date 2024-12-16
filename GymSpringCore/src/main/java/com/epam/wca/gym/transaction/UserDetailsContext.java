package com.epam.wca.gym.transaction;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserDetailsContext {
    private final ThreadLocal<String> userDetailsHolder =
            new ThreadLocal<>();

    public void setUsername(String username) {
        userDetailsHolder.set(username);
    }

    public String getUsername() {
        return userDetailsHolder.get();
    }

    public void clear() {
        userDetailsHolder.remove();
    }
}
