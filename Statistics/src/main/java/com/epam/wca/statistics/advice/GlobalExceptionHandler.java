package com.epam.wca.statistics.advice;

import com.epam.wca.common.gymcommon.exception.InternalErrorException;
import com.epam.wca.statistics.exception.BadDataException;
import com.epam.wca.statistics.exception.NoDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadDataException.class)
    public ResponseEntity<String> handleBadDataException(BadDataException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<String> handleInternalErrorException(InternalErrorException ex) {
        log.error(ex.getMessage());

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoDataException.class)
    public ResponseEntity<String> handleNoDataException(NoDataException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
