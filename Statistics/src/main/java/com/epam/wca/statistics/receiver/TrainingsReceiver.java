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
    private final TrainerService trainerService;
    private final Validator validator;

    @Logging
    @JmsListener(destination = AppConstants.TRAINING_ADD_QUEUE)
    public void receiveAddTraining(
            TrainerTrainingAddDTO trainingAddDTO,
            Message message
    ) {
        try {
            validate(trainingAddDTO);

            String transactionId = message.getStringProperty(AppConstants.TRANSACTION_ID_PROPERTY);

            if (transactionId == null) {
                log.error("Not Transaction Id found in message received.");
                return;
            }

            TransactionContext.setTransactionId(transactionId);

            if (trainingAddDTO.username().equals("ethan.white")) {
                throw new RuntimeException("Can not create training with this dude.");
            }

            trainerService.addNewTraining(trainingAddDTO);

            TransactionContext.clear();
        } catch (ConstraintViolationException e) {
            log.error("Not valid message received. Error: {}", e.getMessage());
        } catch (JMSException e) {
            log.error("Error processing message. Error: {}", e.getMessage());
        }
    }

    @Logging
    @JmsListener(destination = AppConstants.TRAINING_DELETE_QUEUE)
    public void receiveDeleteTraining(
            TrainersTrainingsDeleteDTO trainingsDeleteDTO,
            Message message
    ) {
        try {
            validate(trainingsDeleteDTO);

            String transactionId = message.getStringProperty(AppConstants.TRANSACTION_ID_PROPERTY);

            if (transactionId == null) {
                log.error("Not Transaction Id found in message received.");
                return;
            }

            TransactionContext.setTransactionId(transactionId);

            trainerService.deleteTrainings(trainingsDeleteDTO);

            TransactionContext.clear();
        } catch (ConstraintViolationException e) {
            log.error("Not valid message received. Error: {}", e.getMessage());
        } catch (JMSException e) {
            log.error("Error processing message. Error: {}", e.getMessage());
        }
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
