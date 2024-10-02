package com.epam.wca.gym.app;

import com.epam.wca.gym.config.AppConfig;
import com.epam.wca.gym.facade.AppRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @deprecated This class is deprecated. It was used for running console application.
 * Use {@link WebGymApplication} instead
 */
@Deprecated(since = "2.0")
@Slf4j
public class GymApplication {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            AppRunner appRunner = context.getBean(AppRunner.class);
            appRunner.run();
            log.info("Application Closing...");
        }
    }
}
