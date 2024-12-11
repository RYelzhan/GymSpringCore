package com.epam.wca.statistics.receiver;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.logging.TransactionContext;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.common.gymcommon.util.AppConstants;
import com.epam.wca.statistics.service.TrainerService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingsReceiver {
    public static final String NO_TRANSACTION_ID_FOUND = "Not Transaction Id found in message received.";
    private final TrainerService trainerService;
    private final Validator validator;

    @Logging
    @JmsListener(destination = AppConstants.TRAINING_ADD_QUEUE)
    public void receiveAddTraining(
            TrainerTrainingAddDTO trainingAddDTO,
            Message message
    ) throws JMSException {
        try {
            validate(trainingAddDTO);

            String transactionId = getTransactionId(message);

            TransactionContext.setTransactionId(transactionId);

            trainerService.addNewTraining(trainingAddDTO);

            TransactionContext.clear();
        } catch (ConstraintViolationException e) {
            log.error("Not valid message received. Error: {}", e.getMessage());

            throw new JMSException("Not valid message received. Error: " + e.getMessage());
        } catch (JMSException e) {
            log.error("Error processing message. Error: {}", e.getMessage());

            throw new JMSException("Error processing message. Error: " + e.getMessage());
        }
    }

    @Logging
    @JmsListener(destination = AppConstants.TRAINING_DELETE_QUEUE)
    public void receiveDeleteTraining(
            TrainersTrainingsDeleteDTO trainingsDeleteDTO,
            Message message
    ) throws JMSException {
        try {
            validate(trainingsDeleteDTO);

            String transactionId = getTransactionId(message);

            TransactionContext.setTransactionId(transactionId);

            trainerService.deleteTrainings(trainingsDeleteDTO);

            TransactionContext.clear();
        } catch (ConstraintViolationException e) {
            log.error("Not valid message received. Error: {}", e.getMessage());

            throw new JMSException("Not valid message received. Error: " + e.getMessage());
        } catch (JMSException e) {
            log.error("Error processing message. Error: {}", e.getMessage());

            throw new JMSException("Error processing message. Error: " + e.getMessage());
        }
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
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
