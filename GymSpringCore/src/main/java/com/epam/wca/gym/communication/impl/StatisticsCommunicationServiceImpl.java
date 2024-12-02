package com.epam.wca.gym.communication.impl;

import com.epam.wca.common.gymcommon.logging.TransactionContext;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerWorkloadSummary;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import com.epam.wca.gym.communication.StatisticsCommunicationService;
import com.epam.wca.gym.communication.feign.StatisticsClient;
import com.epam.wca.gym.exception.ServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatisticsCommunicationServiceImpl implements StatisticsCommunicationService {
    public static final String STATISTICS_SERVICE_UNAVAILABLE_USER_MESSAGE =
            "Training Creation is impossible right now. Please wait, and retry after some time.";

    private final StatisticsClient statisticsClient;

    @Override
    @CircuitBreaker(name = "StatisticsService", fallbackMethod = "fallbackResponse")
    @Retry(name = "StatisticsService")
    public void deleteTrainings(TrainersTrainingsDeleteDTO trainingsDeleteDTO) {
        statisticsClient.deleteTrainings(trainingsDeleteDTO);
    }

    @Override
    @CircuitBreaker(name = "StatisticsService", fallbackMethod = "fallbackResponse")
    @Retry(name = "StatisticsService")
    public void addNewTraining(TrainerTrainingAddDTO trainingAddDTO) {
        statisticsClient.addNewTraining(trainingAddDTO);
    }

    @Override
    @CircuitBreaker(name = "StatisticsService", fallbackMethod = "fallbackResponse")
    @Retry(name = "StatisticsService")
    public TrainerWorkloadSummary getWorkload(String username) {
        return statisticsClient.getWorkload(username);
    }

    public void fallbackResponse(TrainersTrainingsDeleteDTO trainingsDeleteDTO, Throwable e) {
        log.error("TransactionId: {}, Fallback for getWorkload: {}",
                TransactionContext.getTransactionId(), e.getMessage());

        throw new ServiceUnavailableException(STATISTICS_SERVICE_UNAVAILABLE_USER_MESSAGE);
    }

    public void fallbackResponse(TrainerTrainingAddDTO trainingAddDTO, Throwable e) {
        log.error("TransactionId: {}, Fallback for getWorkload: {}",
                TransactionContext.getTransactionId(), e.getMessage());

        throw new ServiceUnavailableException(STATISTICS_SERVICE_UNAVAILABLE_USER_MESSAGE);
    }

    public TrainerWorkloadSummary fallbackResponse(String username, Throwable e) {
        log.error("TransactionId: {}, Fallback for getWorkload: {}",
                TransactionContext.getTransactionId(), e.getMessage());

        throw new ServiceUnavailableException(STATISTICS_SERVICE_UNAVAILABLE_USER_MESSAGE);
    }
}
