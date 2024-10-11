package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.user.UserActivationDTO;
import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.UserService;
import com.epam.wca.gym.util.ResponseMessages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Value("${gym.api.request.attribute.user}")
    private String authenticatedUserRequestAttributeName;

    @Operation(
            summary = "Return User username",
            description = "First ever endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Returns authenticated user's username."
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @GetMapping("/info")
    public String getUserInfo(HttpServletRequest request) {
        var authenticatedUser = (User) request.getAttribute(authenticatedUserRequestAttributeName);

        return authenticatedUser.getUserName();
    }

    @Operation(
            summary = "Change User Password"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Password changed successfully."
    )
    @ApiResponse(
            responseCode = "400",
            description = ResponseMessages.INVALID_INPUT_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @PutMapping(value = "/change/password", consumes = MediaType.APPLICATION_JSON_VALUE)
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
    @Operation(
            summary = "Activate or Deactivate User"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User activation status updated successfully."
    )
    @ApiResponse(
            responseCode = "400",
            description = ResponseMessages.INVALID_INPUT_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "401",
            description = ResponseMessages.UNAUTHORIZED_ACCESS_DESCRIPTION
    )
    @PatchMapping(value = "/change/active", consumes = MediaType.APPLICATION_JSON_VALUE)
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
