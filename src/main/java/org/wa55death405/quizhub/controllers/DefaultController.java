package org.wa55death405.quizhub.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wa55death405.quizhub.dto.StandardApiResponse;
import org.wa55death405.quizhub.enums.StandardApiStatus;

@RestController
public class DefaultController {
    @GetMapping
    public ResponseEntity<StandardApiResponse<Void>> healthCheck() {
        return ResponseEntity.ok(new StandardApiResponse<>(StandardApiStatus.SUCCESS, "QuizHub API is running"));
    }
}
