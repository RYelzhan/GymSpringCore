package com.epam.wca.gym.transaction;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserDetailsContext {
    private final ThreadLocal<Long> userDetailsHolder =
            new ThreadLocal<>();

    public void setUserId(Long id) {
        userDetailsHolder.set(id);
    }

    public Long getUserId() {
        return userDetailsHolder.get();
    }

    public void clear() {
        userDetailsHolder.remove();
    }
}
