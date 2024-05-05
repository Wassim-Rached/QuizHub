package org.wa55death405.quizhub.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wa55death405.quizhub.dto.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.StandardApiResponse;
import org.wa55death405.quizhub.enums.StandardApiStatus;
import org.wa55death405.quizhub.services.QuizService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/{quizId}/start")
    public ResponseEntity<StandardApiResponse<Integer>> startQuizAttempt(@PathVariable Integer quizId) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt started successfully",quizService.startQuizAttempt(quizId)), HttpStatus.CREATED);
    }

    @PostMapping("/{quizAttemptId}/submit")
    public ResponseEntity<StandardApiResponse<Void>> submitQuestionAttempts(@RequestBody List<QuestionAttemptSubmissionDTO> questionAttemptSubmissions, @PathVariable Integer quizAttemptId) {
        quizService.submitQuestionAttempts(questionAttemptSubmissions, quizAttemptId);
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Question attempts submitted successfully",null), HttpStatus.ACCEPTED);
    }

    @PostMapping("/{quizAttemptId}/finish")
    public ResponseEntity<StandardApiResponse<Float>> finishQuizAttempt(@PathVariable Integer quizAttemptId) {
    return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt finished and processed successfully",quizService.finishQuizAttempt(quizAttemptId)), HttpStatus.OK);
    }

}
