package com.epam.wca.gym.controller.impl;

import com.epam.wca.gym.controller.UserController;
import com.epam.wca.gym.dto.user.UserActivationDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;
    @Value("${gym.api.request.attribute.user}")
    private String authenticatedUserRequestAttributeName;

    @Override
    public String getUserInfo(HttpServletRequest request) {
        var authenticatedUser = (User) request.getAttribute(authenticatedUserRequestAttributeName);

        return authenticatedUser.getUsername();
    }

    @Override
    public String changeUserPassword(
            @RequestBody @Valid UserUpdateDTO userDTO,
            HttpServletRequest request
    ) {
        var authenticatedUser = (User) request.getAttribute(authenticatedUserRequestAttributeName);

        userService.update(authenticatedUser, userDTO);

        return "Password Changed Successfully";
    }

    // TODO: consider ...Query instead of some ...DTO
    // query is better suited for queries to database. e.g. Filter, Search
    @Override
    public String activateDeactivateUser(
            @RequestBody @Valid UserActivationDTO userDTO,
            HttpServletRequest request
    ) {
        var authenticatedUser = (User) request.getAttribute(authenticatedUserRequestAttributeName);

        userService.update(authenticatedUser, userDTO);

        return "Is Active Updated Successfully";
    }
}

// TODO: change manual training type validation checks in code with this:
//

//    @Target({ElementType.FIELD, ElementType.PARAMETER})
//    @Retention(RetentionPolicy.RUNTIME)
//    @Constraint(validatedBy = TrainingTypeValidator.class)
//    public @interface ValidTrainingType {
//
//        String message() default "Invalid training type"; // Default validation message
//
//        Class<?>[] groups() default {}; // Required for grouping constraints
//
//        Class<? extends Payload>[] payload() default {}; // Can be used by clients to assign custom payload objects
//    }

//  public class TrainingTypeValidator implements ConstraintValidator<ValidTrainingType, String> {
//
//    private final TrainingTypeDAO trainingTypeDAO;
//    @Override
//    public boolean isValid(String trainingType, ConstraintValidatorContext context) {
//        if (trainingType == null || trainingType.trim().isEmpty()) {
//            return true;
//        }
//        return trainingTypeDAO.findByName(trainingType.toUpperCase()).isPresent();
//    }
//}
