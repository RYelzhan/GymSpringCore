package com.epam.wca.gym.aop.validation;

import com.epam.wca.gym.repository.TrainingTypeRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingTypeValidator implements ConstraintValidator<ValidTrainingType, String> {
    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public boolean isValid(String trainingType, ConstraintValidatorContext constraintValidatorContext) {
        if (trainingTypeNotRequired(trainingType)) {
            return true;
        }
        return trainingTypeRepository.findTrainingTypeByType(trainingType).isPresent();
    }

    private boolean trainingTypeNotRequired(String trainingType) {
        return trainingType == null || trainingType.trim().isEmpty();
    }
}
