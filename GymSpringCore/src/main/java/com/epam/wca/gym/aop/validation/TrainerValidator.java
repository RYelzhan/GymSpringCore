package com.epam.wca.gym.aop.validation;

import com.epam.wca.gym.repository.TrainerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainerValidator implements ConstraintValidator<ValidTrainer, String> {
    private final TrainerRepository trainerRepository;

    @Override
    public boolean isValid(String trainerUsername, ConstraintValidatorContext context) {
        if (usernameNotRequired(trainerUsername)) {
            return true;
        }
        return trainerRepository.findTrainerByUserName(trainerUsername).isPresent();
    }

    private boolean usernameNotRequired(String trainerUsername) {
        return trainerUsername == null || trainerUsername.trim().isEmpty();
    }
}
