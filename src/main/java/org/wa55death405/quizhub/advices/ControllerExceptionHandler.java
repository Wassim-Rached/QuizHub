package org.wa55death405.quizhub.advices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.wa55death405.quizhub.exceptions.InvalidQuestionResponseException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(InvalidQuestionResponseException.class)
    public ResponseEntity<?> handleInvalidQuestionResponseException(InvalidQuestionResponseException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
