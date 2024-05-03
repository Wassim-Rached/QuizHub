package org.wa55death405.quizhub.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wa55death405.quizhub.dto.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.services.QuizService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/{quizId}/start")
    public ResponseEntity<Integer> startQuizAttempt(@PathVariable Integer quizId) {
        return ResponseEntity.ok(quizService.startQuizAttempt(quizId));
    }

    @PostMapping("/{quizAttemptId}/submit")
    public ResponseEntity<Void> submitQuestionAttempts(@RequestBody List<QuestionAttemptSubmissionDTO> questionAttemptSubmissions, @PathVariable Integer quizAttemptId) {
        quizService.submitQuestionAttempts(questionAttemptSubmissions, quizAttemptId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{quizAttemptId}/finish")
    public ResponseEntity<Float> finishQuizAttempt(@PathVariable Integer quizAttemptId) {
        return ResponseEntity.ok(quizService.finishQuizAttempt(quizAttemptId));
    }

}
