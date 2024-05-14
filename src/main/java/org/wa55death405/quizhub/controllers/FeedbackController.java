package org.wa55death405.quizhub.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wa55death405.quizhub.dto.StandardApiResponse;
import org.wa55death405.quizhub.entities.Feedback;
import org.wa55death405.quizhub.repositories.FeedbackRepository;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackRepository feedbackRepository;

//     create feedback
    @PostMapping
    public ResponseEntity<StandardApiResponse<Void>> createFeedback(@RequestBody CreateFeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setContent(request.content);
        feedback.setRating(request.rating);
        feedback.setEmail(request.email);
        feedbackRepository.save(feedback);
        return ResponseEntity.noContent().build();
    }

    public record CreateFeedbackRequest(String content, Integer rating, String email) { }
}
