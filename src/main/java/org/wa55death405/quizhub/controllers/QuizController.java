package org.wa55death405.quizhub.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.dto.StandardApiResponse;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptResultDTO;
import org.wa55death405.quizhub.enums.StandardApiStatus;
import org.wa55death405.quizhub.interfaces.services.IQuizService;
import org.wa55death405.quizhub.services.QuizService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {

    private final IQuizService quizService;

    // core api's
    @PostMapping("/{quizId}/start")
    public ResponseEntity<StandardApiResponse<Integer>> startQuizAttempt(@PathVariable Integer quizId) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt started successfully",quizService.startQuizAttempt(quizId)), HttpStatus.CREATED);
    }

    @PostMapping("/attempt/{quizAttemptId}/submit")
    public ResponseEntity<StandardApiResponse<Void>> submitQuestionAttempts(@RequestBody List<QuestionAttemptSubmissionDTO> body, @PathVariable Integer quizAttemptId) {
        quizService.submitQuestionAttempts(body, quizAttemptId);
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Question attempts submitted successfully",null), HttpStatus.ACCEPTED);
    }

    @PostMapping("/attempt/{quizAttemptId}/finish")
    public ResponseEntity<StandardApiResponse<Float>> finishQuizAttempt(@PathVariable Integer quizAttemptId) {
    return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt finished and processed successfully",quizService.finishQuizAttempt(quizAttemptId)), HttpStatus.OK);
    }

    // additional api's
    @PostMapping
    public ResponseEntity<StandardApiResponse<Integer>> createQuiz(@RequestBody QuizCreationDTO body) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz created successfully",quizService.createQuiz(body)), HttpStatus.CREATED);
    }

    @GetMapping("/attempt/{quizAttemptId}/result")
    public ResponseEntity<StandardApiResponse<QuizAttemptResultDTO>> getQuizAttemptResult(@PathVariable Integer quizAttemptId) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt result fetched successfully",quizService.getQuizAttemptResult(quizAttemptId)), HttpStatus.OK);
    }


}
