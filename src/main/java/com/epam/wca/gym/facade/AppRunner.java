package com.epam.wca.gym.facade;

import com.epam.wca.gym.facade.user.UserSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppRunner {
    @NonNull
    private GymFacade facade;
    @NonNull
    private final UserSession userSession;

    public void run() {
        log.info("Application Started Successfully!");

        boolean appRunning = true;

        while (appRunning) {
            if (userSession.isLoggedIn()) {
                facade.handleLoggedInState();
            } else {
                appRunning = facade.handleLoggedOutState();
            }
        }
    }
}