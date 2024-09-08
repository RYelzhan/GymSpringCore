package com.epam.wca.gym.facade;

import com.epam.wca.gym.config.AppConfig;
import com.epam.wca.gym.repository.impl.UsernameDAO;
import com.epam.wca.gym.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class MainFacade {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);
        TrainingTypeService trainingTypeService = context.getBean(TrainingTypeService.class);
        UserService userService = context.getBean(UserService.class);
        UsernameDAO usernameDAO = context.getBean(UsernameDAO.class);

        System.out.println(traineeService.findAll());
        System.out.println(trainerService.findAll());
        System.out.println(trainingService.findAll());
        System.out.println(trainingTypeService.findAll());
        System.out.println(userService.findAll());
        System.out.println(usernameDAO.findAll());
    }
}
