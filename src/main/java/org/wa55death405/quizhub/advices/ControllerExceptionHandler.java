package org.wa55death405.quizhub.advices;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.wa55death405.quizhub.dto.StandardApiResponse;
import org.wa55death405.quizhub.enums.StandardApiStatus;
import org.wa55death405.quizhub.exceptions.InputValidationException;
import org.wa55death405.quizhub.exceptions.RateLimitReachedException;

import java.io.FileNotFoundException;


/*
    this class is the controller advice
    it is used to handle the exceptions thrown by the controllers
 */

/*
    TODO:
      separate the exception handlers into different classes
      * one for predefined spring or library exceptions
      * one for custom exceptions
 */
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

    @ExceptionHandler(RateLimitReachedException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleRateLimitReachedException(RateLimitReachedException e) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.FAILURE, e.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleFileNotFoundException(FileNotFoundException e) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.FAILURE, "File not found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.FAILURE, "Invalid value for parameter "+ e.getPropertyName() +". Expected type: "+ e.getRequiredType()), HttpStatus.BAD_REQUEST);
    }
}