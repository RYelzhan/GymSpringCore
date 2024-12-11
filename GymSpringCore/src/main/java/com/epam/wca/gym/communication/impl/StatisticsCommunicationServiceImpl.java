package com.epam.wca.gym.communication.impl;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.common.gymcommon.logging.TransactionContext;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.common.gymcommon.util.AppConstants;
import com.epam.wca.gym.communication.StatisticsCommunicationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsCommunicationServiceImpl implements StatisticsCommunicationService {
    private final JmsTemplate jmsTemplate;

    @Override
    @Logging
    @Transactional(transactionManager = "jmsTransactionManager")
    @CircuitBreaker(name = "statistics", fallbackMethod = "fallback")
    @Retry(name = "statistics")
    public void deleteTrainings(TrainersTrainingsDeleteDTO trainingsDeleteDTO) {
        jmsTemplate.convertAndSend(
                AppConstants.TRAINING_DELETE_QUEUE,
                trainingsDeleteDTO,
                message -> {
                    message.setStringProperty(
                            AppConstants.TRANSACTION_ID_PROPERTY,
                            TransactionContext.getTransactionId()
                    );
                    return message;
                });
    }

    private static void logErrorMessage(Throwable e) {
        log.error("TransactionId: {} | Error of sending message to Message Queue. Error: {}",
                TransactionContext.getTransactionId(), e.getMessage());
    }

    @Override
    @Logging
    @Transactional(transactionManager = "jmsTransactionManager")
    @CircuitBreaker(name = "statistics", fallbackMethod = "fallback")
    @Retry(name = "statistics")
    public void addNewTraining(TrainerTrainingAddDTO trainingAddDTO) {
        jmsTemplate.convertAndSend(
                AppConstants.TRAINING_ADD_QUEUE,
                trainingAddDTO,
                message -> {
                    message.setStringProperty(
                            AppConstants.TRANSACTION_ID_PROPERTY,
                            TransactionContext.getTransactionId()
                    );
                    return message;
                });
    }

    public void fallback(TrainersTrainingsDeleteDTO trainingsDeleteDTO, Throwable throwable) {
        logErrorMessage(throwable);

        throw new InternalErrorException("Deletion is impossible right now. Retry later.");
    }

    public void fallback(TrainerTrainingAddDTO trainingAddDTO, Throwable throwable) {
        logErrorMessage(throwable);

        throw new InternalErrorException("Addition is impossible right now. Retry later.");
    }
}
