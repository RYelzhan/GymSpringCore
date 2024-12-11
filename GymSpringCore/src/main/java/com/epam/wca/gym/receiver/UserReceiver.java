package com.epam.wca.gym.receiver;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.util.AppConstants;
import com.epam.wca.gym.repository.TraineeRepository;
import com.epam.wca.gym.repository.TrainerRepository;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReceiver {
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;

    @Logging
    @JmsListener(destination = AppConstants.TRAINEE_DELETE_QUEUE)
    public void receiveTraineeDelete(
            String username,
            Message message
    ) {
        var trainee = traineeRepository.findTraineeByUsername(username);

    }
}
