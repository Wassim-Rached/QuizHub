package org.wa55death405.quizhub.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wa55death405.quizhub.dto.StandardApiResponse;
import org.wa55death405.quizhub.enums.StandardApiStatus;

/*
    this class is the default controller
    it contains the default api of the application
 */

@RestController
public class DefaultController {

    @Value("${app.APP_VERSION:0.0.0}")
    private String APP_VERSION;

    /*
        this api is used to check the health of the application
        it returns a success message if the application is running
     */
    @GetMapping
    public ResponseEntity<StandardApiResponse<Void>> healthCheck() {
        return ResponseEntity.ok(new StandardApiResponse<>(StandardApiStatus.SUCCESS, "QuizHub API is running on version " + APP_VERSION));
    }
}
