package com.epam.wca.authentication.communication.impl;

import brave.Tracing;
import com.epam.wca.authentication.communication.GymCommunicationService;
import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.common.gymcommon.logging.TransactionContext;
import com.epam.wca.common.gymcommon.util.AppConstants;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GymCommunicationServiceImpl implements GymCommunicationService {
    private final JmsTemplate jmsTemplate;
    private final Tracing tracing;

    @Override
    @Logging
    @Transactional(transactionManager = "jmsTransactionManager")
    @CircuitBreaker(name = "gym", fallbackMethod = "fallbackTrainee")
    public void deleteTrainee(String username) {
        jmsTemplate.convertAndSend(
                AppConstants.TRAINEE_DELETE_QUEUE,
                username,
                message -> {
                    attachMessageProperties(message);

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
    @CircuitBreaker(name = "gym", fallbackMethod = "fallbackTrainer")
    public void deleteTrainer(String username) {
        jmsTemplate.convertAndSend(
                AppConstants.TRAINER_DELETE_QUEUE,
                username,
                message -> {
                    attachMessageProperties(message);

                    return message;
                });
    }

    public void fallbackTrainee(String username, Throwable throwable) {
        logErrorMessage(throwable);

        throw new InternalErrorException("Deletion is impossible right now. Retry later.");
    }

    public void fallbackTrainer(String username, Throwable throwable) {
        logErrorMessage(throwable);

        throw new InternalErrorException("Deletion is impossible right now. Retry later.");
    }

    private void attachMessageProperties(Message message) throws JMSException {
        message.setStringProperty(
                AppConstants.TRANSACTION_ID_PROPERTY,
                TransactionContext.getTransactionId()
        );

        var context = tracing.currentTraceContext().get();
        if (context != null) {
            message.setStringProperty(AppConstants.TRACE_ID_HEADER, context.traceIdString());
            message.setStringProperty(AppConstants.SPAN_ID_HEADER, context.spanIdString());
        }
    }
}
