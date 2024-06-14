package org.wa55death405.quizhub.advices;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.wa55death405.quizhub.dto.StandardApiResponse;
import org.wa55death405.quizhub.entities.ErrorLog;
import org.wa55death405.quizhub.enums.StandardApiStatus;
import org.wa55death405.quizhub.exceptions.InputValidationException;
import org.wa55death405.quizhub.exceptions.IrregularBehaviorException;
import org.wa55death405.quizhub.exceptions.RateLimitReachedException;
import org.wa55death405.quizhub.repositories.ErrorLogRepository;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Objects;


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
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final ErrorLogRepository errorLogRepository;

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

    @ExceptionHandler(IrregularBehaviorException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleIrregularBehaviorException(IrregularBehaviorException e) {
        ErrorLog errorLog = ErrorLog.builder()
                .exceptionMessage(e.getMessage())
                .stackTrace(ErrorLog.getStackTraceAsString(e))
                .build();
        errorLogRepository.save(errorLog);
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.FAILURE, "Something Irregular just happened"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String rootCauseMessage = Objects.requireNonNull(e.getRootCause()).getMessage();

        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.FAILURE, rootCauseMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardApiResponse<Void>> handleException(Exception e) {
        ErrorLog errorLog = ErrorLog.builder()
                .exceptionMessage(e.getMessage())
                .stackTrace(ErrorLog.getStackTraceAsString(e))
                .timestamp(LocalDateTime.now())
                .build();
        errorLogRepository.save(errorLog);
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.FAILURE, "Something Went Wrong!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}