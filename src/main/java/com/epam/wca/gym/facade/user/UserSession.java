package com.epam.wca.gym.facade.user;

import com.epam.wca.gym.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserSession {
    private User user;

    public boolean isLoggedIn() {
        return user != null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void logOut() {
        user = null;
    }
}

