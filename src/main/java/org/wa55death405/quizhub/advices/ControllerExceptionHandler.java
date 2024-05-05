package org.wa55death405.quizhub.advices;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.wa55death405.quizhub.dto.StandardApiResponse;
import org.wa55death405.quizhub.enums.StandardApiStatus;
import org.wa55death405.quizhub.exceptions.InputValidationException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(InputValidationException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleInvalidQuestionResponseException(InputValidationException e) {
        return ResponseEntity.badRequest().body(new StandardApiResponse<>(StandardApiStatus.FAILURE, e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.FAILURE, e.getMessage()), HttpStatus.NOT_FOUND);
    }

}