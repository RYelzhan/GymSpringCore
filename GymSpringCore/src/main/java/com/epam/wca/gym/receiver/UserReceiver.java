package com.epam.wca.gym.receiver;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.logging.TransactionContext;
import com.epam.wca.common.gymcommon.util.AppConstants;
import com.epam.wca.gym.repository.TraineeRepository;
import com.epam.wca.gym.repository.TrainerRepository;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserReceiver {
    public static final String NO_TRANSACTION_ID_FOUND = "Not Transaction Id found in message received.";

    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;

    @Logging
    @JmsListener(destination = AppConstants.TRAINEE_DELETE_QUEUE)
    public void receiveTraineeDelete(
            String username,
            Message message
    ) throws JMSException {
        try {
            var trainee = traineeRepository.findTraineeByUsername(username)
                    .orElseThrow(() -> {
                        log.error("Not valid message received. Error: trainee does not exist.");

                        return new JMSException("Not valid message received. Error: trainee does not exist.");
                    });

            String transactionId = getTransactionId(message);

            TransactionContext.setTransactionId(transactionId);

            traineeService.deleteById(trainee.getId());

            TransactionContext.clear();
        } catch (JMSException e) {
            log.error("Error processing message. Error: {}", e.getMessage());

            throw new JMSException("Error processing message. Error: " + e.getMessage());
        }
    }

    @Logging
    @JmsListener(destination = AppConstants.TRAINER_DELETE_QUEUE)
    public void receiveTrainerDelete(
            String username,
            Message message
    ) throws JMSException {
        try {
            var trainer = trainerRepository.findTrainerByUsername(username)
                    .orElseThrow(() -> {
                        log.error("Not valid message received. Error: trainer does not exist.");

                        return new JMSException("Not valid message received. Error: trainer does not exist.");
                    });

            String transactionId = getTransactionId(message);

            TransactionContext.setTransactionId(transactionId);

            trainerService.deleteById(trainer.getId());

            TransactionContext.clear();
        } catch (JMSException e) {
            log.error("Error processing message. Error: {}", e.getMessage());

            throw new JMSException("Error processing message. Error: " + e.getMessage());
        }
    }

    private static String getTransactionId(Message message) throws JMSException {
        String transactionId = message.getStringProperty(AppConstants.TRANSACTION_ID_PROPERTY);

        if (transactionId == null) {
            log.error(NO_TRANSACTION_ID_FOUND);

            throw new JMSException(NO_TRANSACTION_ID_FOUND);
        }
        return transactionId;
    }
}
