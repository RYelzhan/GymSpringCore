package com.epam.wca.gym.receiver;

import com.epam.wca.common.gymcommon.aop.Logging;
import com.epam.wca.common.gymcommon.logging.TransactionContext;
import com.epam.wca.common.gymcommon.util.AppConstants;
import com.epam.wca.gym.repository.TraineeRepository;
import com.epam.wca.gym.repository.TrainerRepository;
import com.epam.wca.gym.service.TraineeService;
import com.epam.wca.gym.service.TrainerService;
import jakarta.jms.JMSException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
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
            @Header(name = AppConstants.TRANSACTION_ID_PROPERTY) String transactionId,
            @Header(name = AppConstants.SPAN_ID_HEADER) String spanId,
            @Header(name = AppConstants.TRACE_ID_HEADER) String traceId
    ) throws JMSException {
        log.info("Request with spanId: {} and traceID: {}",
                spanId, traceId);

        var trainee = traineeRepository.findTraineeByUsername(username)
                .orElseThrow(() -> {
                    log.error("Not valid message received. Error: trainee does not exist.");

                    return new JMSException("Not valid message received. Error: trainee does not exist.");
                });

        TransactionContext.setTransactionId(transactionId);

        traineeService.deleteById(trainee.getId());

        TransactionContext.clear();
    }

    @Logging
    @JmsListener(destination = AppConstants.TRAINER_DELETE_QUEUE)
    public void receiveTrainerDelete(
            String username,
            @Header(name = AppConstants.TRANSACTION_ID_PROPERTY) String transactionId,
            @Header(name = AppConstants.SPAN_ID_HEADER) String spanId,
            @Header(name = AppConstants.TRACE_ID_HEADER) String traceId
    ) throws JMSException {
        log.info("Request with spanId: {} and traceID: {}",
                spanId, traceId);

        var trainer = trainerRepository.findTrainerByUsername(username)
                .orElseThrow(() -> {
                    log.error("Not valid message received. Error: trainer does not exist.");

                    return new JMSException("Not valid message received. Error: trainer does not exist.");
                });

        TransactionContext.setTransactionId(transactionId);

        trainerService.deleteById(trainer.getId());

        TransactionContext.clear();
    }
}
