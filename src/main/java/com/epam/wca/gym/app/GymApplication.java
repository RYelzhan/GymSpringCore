package com.epam.wca.gym.app;

import com.epam.wca.gym.config.AppConfig;
import com.epam.wca.gym.facade.AppRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class GymApplication {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {
            AppRunner appRunner = context.getBean(AppRunner.class);
            appRunner.run();
            log.info("Application Closing...");
        }
    }
}
