package com.epam.wca.gym.communication.impl;

import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.common.gymcommon.logging.TransactionContext;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.common.gymcommon.util.AppConstants;
import com.epam.wca.gym.communication.StatisticsCommunicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsCommunicationServiceImpl implements StatisticsCommunicationService {
    private final JmsTemplate jmsTemplate;

    @Override
    public void deleteTrainings(TrainersTrainingsDeleteDTO trainingsDeleteDTO) {
        try {
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
        } catch(JmsException e) {
            logErrorMessage(e);

            throw new InternalErrorException("Deletion is impossible right now. Retry later.");
        }
    }

    private static void logErrorMessage(JmsException e) {
        log.error("TransactionId: {} | Error of sending message to Message Queue. Error: {}",
                TransactionContext.getTransactionId(), e.getMessage());
    }

    @Override
    public void addNewTraining(TrainerTrainingAddDTO trainingAddDTO) {
        try {
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
        } catch(JmsException e) {
            logErrorMessage(e);

            throw new InternalErrorException("Creation is impossible right now. Retry later.");
        }
    }
}
