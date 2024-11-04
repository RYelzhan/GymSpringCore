package com.epam.wca.gym.advice;

import com.epam.wca.gym.exception.AuthenticationException;
import com.epam.wca.gym.exception.BadControllerRequestException;
import com.epam.wca.gym.exception.ControllerValidationException;
import com.epam.wca.gym.exception.ForbiddenActionException;
import com.epam.wca.gym.exception.InternalErrorException;
import com.epam.wca.gym.exception.ProfileNotFoundException;
import com.epam.wca.gym.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String INVALID_DATE_FORMAT = "Invalid date format. Please use the correct format (e.g., %s ).";
    private static final String URL_NOT_FOUND_MESSAGE = "The requested URL was not found on the server.";
    private static final String SERVER_ERROR_MESSAGE = "Error happened on server side.";

    @ExceptionHandler(ControllerValidationException.class)
    public ResponseEntity<String> handleMethodArgumentValidationExceptions(ControllerValidationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.I_AM_A_TEAPOT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<String> handleForbiddenActionException(ForbiddenActionException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable() {
        String message = String.format(INVALID_DATE_FORMAT, AppConstants.DEFAULT_DATE_FORMAT);

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadControllerRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadControllerRequestException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<String> handleInternalErrorException(InternalErrorException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidLoginAttempt(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleInvalidLoginAttempt(AuthenticationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<String> handleInvalidLoginAttempt(ProfileNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException() {
        return new ResponseEntity<>(URL_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFoundException() {
        return new ResponseEntity<>(URL_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleServerError(Exception e) {
        log.error(e.getMessage());

        return new ResponseEntity<>(SERVER_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
