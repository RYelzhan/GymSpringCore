package com.epam.wca.gym.app;

import com.epam.wca.gym.config.AppConfig;
import com.epam.wca.gym.facade.GymFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class GymApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        GymFacade gymFacade = context.getBean(GymFacade.class);
        gymFacade.run();
        log.info("Application Closing...");
        context.close();
    }
}
