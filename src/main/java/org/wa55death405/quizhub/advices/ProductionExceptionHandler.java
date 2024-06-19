package org.wa55death405.quizhub.advices;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.wa55death405.quizhub.dto.StandardApiResponse;
import org.wa55death405.quizhub.entities.ErrorLog;
import org.wa55death405.quizhub.enums.StandardApiStatus;
import org.wa55death405.quizhub.exceptions.IrregularBehaviorException;
import org.wa55death405.quizhub.repositories.ErrorLogRepository;

import java.time.LocalDateTime;

@ControllerAdvice
@Profile({"prod"})
@RequiredArgsConstructor
public class ProductionExceptionHandler {

    private final ErrorLogRepository errorLogRepository;

    @ExceptionHandler(IrregularBehaviorException.class)
    public ResponseEntity<StandardApiResponse<Void>> handleIrregularBehaviorException(IrregularBehaviorException e) {
        ErrorLog errorLog = ErrorLog.builder()
                .exceptionMessage(e.getMessage())
                .stackTrace(ErrorLog.getStackTraceAsString(e))
                .build();
        errorLogRepository.save(errorLog);
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.FAILURE, "Something Irregular just happened"), HttpStatus.INTERNAL_SERVER_ERROR);
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
