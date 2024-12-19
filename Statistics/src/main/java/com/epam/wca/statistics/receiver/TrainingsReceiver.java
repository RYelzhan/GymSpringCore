package com.epam.wca.statistics.receiver;

import com.epam.wca.common.gymcommon.logging.TransactionContext;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.common.gymcommon.util.AppConstants;
import com.epam.wca.statistics.service.TrainerService;
import jakarta.jms.JMSException;
import jakarta.validation.ConstraintViolation;
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

    @JmsListener(destination = AppConstants.TRAINING_ADD_QUEUE)
    public void receiveAddTraining(
            @Payload TrainerTrainingAddDTO trainingAddDTO,
            @Header(name = AppConstants.TRANSACTION_ID_PROPERTY) String transactionId,
            @Header(name = AppConstants.SPAN_ID_HEADER) String spanId,
            @Header(name = AppConstants.TRACE_ID_HEADER) String traceId
    ) throws JMSException {
        log.info("Request with spanId: {} and traceID: {}",
                spanId, traceId);

        validate(trainingAddDTO);

        TransactionContext.setTransactionId(transactionId);

        trainerService.addNewTraining(trainingAddDTO);

        TransactionContext.clear();
    }

    @JmsListener(destination = AppConstants.TRAINING_DELETE_QUEUE)
    public void receiveDeleteTraining(
            @Payload TrainersTrainingsDeleteDTO trainingsDeleteDTO,
            @Header(name = AppConstants.TRANSACTION_ID_PROPERTY) String transactionId,
            @Header(name = AppConstants.SPAN_ID_HEADER) String spanId,
            @Header(name = AppConstants.TRACE_ID_HEADER) String traceId
    ) throws JMSException {
        log.info("Request with spanId: {} and traceID: {}",
                spanId, traceId);

        validate(trainingsDeleteDTO);

        TransactionContext.setTransactionId(transactionId);

        trainerService.deleteTrainings(trainingsDeleteDTO);

        TransactionContext.clear();
    }

    private <T> void validate(T dto) throws JMSException {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            log.error("Not valid message received. Violations: {}", violations);

            throw new JMSException("Not valid message received. Violations: " + violations);
        }
    }
}
