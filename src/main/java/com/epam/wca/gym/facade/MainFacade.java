package com.epam.wca.gym.facade;

import com.epam.wca.gym.config.StorageConfig;
import com.epam.wca.gym.repository.impl.TraineeDAOImpl;
import com.epam.wca.gym.repository.impl.TrainerDAOImpl;
import com.epam.wca.gym.repository.impl.TrainingDAOImpl;
import com.epam.wca.gym.service.impl.ProfileServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class MainFacade {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(StorageConfig.class);

        TraineeDAOImpl traineeDAO = context.getBean(TraineeDAOImpl.class);
        TrainerDAOImpl trainerDAO = context.getBean(TrainerDAOImpl.class);
        TrainingDAOImpl trainingDAO = context.getBean(TrainingDAOImpl.class);

        System.out.println(traineeDAO.getAll());
        System.out.println(traineeDAO.getUsernameToId());
        System.out.println(traineeDAO.getCurrentMaxId());

        System.out.println("----------------------");

        System.out.println(trainerDAO.getAll());
        System.out.println(trainerDAO.getUsernameToId());
        System.out.println(Arrays.toString(trainerDAO.getCurrentMaxId()));

        System.out.println("----------------------");

        System.out.println(trainingDAO.getAll());
        System.out.println(trainingDAO.getCurrentMaxId());

        System.out.println("----------------------");

        ProfileServiceImpl profileService = context.getBean(ProfileServiceImpl.class);
        System.out.println(profileService.getUsernameCounter());

    }
}
