package com.epam.wca.statistics.transaction;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserDetailsContext {
    private final ThreadLocal<String> usernameHolder =
            new ThreadLocal<>();

    public void setUsername(String id) {
        usernameHolder.set(id);
    }

    public String getUsername() {
        return usernameHolder.get();
    }

    public void clear() {
        usernameHolder.remove();
    }
}
