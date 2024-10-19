package com.epam.wca.gym.aop.validation;

import com.epam.wca.gym.repository.TraineeRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraineeExistsValidator implements ConstraintValidator<TraineeExists, String> {
    private final TraineeRepository traineeRepository;

    @Override
    public boolean isValid(String traineeUsername, ConstraintValidatorContext context) {
        if (traineeUsername == null || traineeUsername.trim().isEmpty()) {
            return false;
        }
        return traineeRepository.findTraineeByUserName(traineeUsername).isPresent();
    }
}
