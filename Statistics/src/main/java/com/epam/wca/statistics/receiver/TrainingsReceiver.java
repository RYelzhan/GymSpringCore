package com.epam.wca.statistics.receiver;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.logging.TransactionContext;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.common.gymcommon.util.AppConstants;
import com.epam.wca.statistics.service.TrainerService;
import jakarta.jms.JMSException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
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
            @Payload TrainerTrainingAddDTO trainingAddDTO,
            @Header(name = AppConstants.TRANSACTION_ID_PROPERTY) String transactionId
    ) throws JMSException {
        try {
            validate(trainingAddDTO);

            TransactionContext.setTransactionId(transactionId);

            trainerService.addNewTraining(trainingAddDTO);

            TransactionContext.clear();
        } catch (ConstraintViolationException e) {
            log.error("Not valid message received. Error: {}", e.getMessage());

            throw new JMSException("Not valid message received. Error: " + e.getMessage());
        }
    }

    @Logging
    @JmsListener(destination = AppConstants.TRAINING_DELETE_QUEUE)
    public void receiveDeleteTraining(
            @Payload TrainersTrainingsDeleteDTO trainingsDeleteDTO,
            @Header(name = AppConstants.TRANSACTION_ID_PROPERTY) String transactionId
    ) throws JMSException {
        try {
            validate(trainingsDeleteDTO);

            TransactionContext.setTransactionId(transactionId);

            trainerService.deleteTrainings(trainingsDeleteDTO);

            TransactionContext.clear();
        } catch (ConstraintViolationException e) {
            log.error("Not valid message received. Error: {}", e.getMessage());

            throw new JMSException("Not valid message received. Error: " + e.getMessage());
        }
    }

    private <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
