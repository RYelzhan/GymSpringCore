package com.epam.wca.gym.aop.validation;

import com.epam.wca.gym.repository.TraineeRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraineeValidator implements ConstraintValidator<ValidTrainee, String> {
    private final TraineeRepository traineeRepository;

    @Override
    public boolean isValid(String traineeUsername, ConstraintValidatorContext context) {
        if (usernameNotRequired(traineeUsername)) {
            return true;
        }
        return traineeRepository.findTraineeByUserName(traineeUsername).isPresent();
    }

    private boolean usernameNotRequired(String traineeUsername) {
        return traineeUsername == null || traineeUsername.trim().isEmpty();
    }
}
