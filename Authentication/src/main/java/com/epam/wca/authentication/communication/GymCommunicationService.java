package com.epam.wca.authentication.communication;

public interface GymCommunicationService {
    void deleteTrainee(String username);

    void deleteTrainer(String username);
}
