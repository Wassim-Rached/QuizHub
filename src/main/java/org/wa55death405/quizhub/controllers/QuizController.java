package org.wa55death405.quizhub.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.dto.StandardApiResponse;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptResultDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptTakingDTO;
import org.wa55death405.quizhub.enums.StandardApiStatus;
import org.wa55death405.quizhub.interfaces.services.IQuizService;

import java.util.List;

/*
    this class is the controller of the quiz
    it contains all the apis related to the quiz
    including the questions and attempts of the quiz
*/

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {

    private final IQuizService quizService;

    /*
        this api is used to start taking a quiz
        it takes the quiz id as a path variable
        and returns the quiz attempt id
     */
    @PostMapping("/{quizId}/start")
    public ResponseEntity<StandardApiResponse<Integer>> startQuizAttempt(@PathVariable Integer quizId) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt started successfully",quizService.startQuizAttempt(quizId).getId()), HttpStatus.CREATED);
    }

    /*
        this api is used to submit the question
        attempts without processing them
        it takes List of 'QuestionAttemptSubmissionDTO'
        and the quiz attempt id as a path variable
     */
    @PostMapping("/attempt/{quizAttemptId}/submit")
    public ResponseEntity<StandardApiResponse<Void>> submitQuestionAttempts(@RequestBody List<QuestionAttemptSubmissionDTO> body, @PathVariable Integer quizAttemptId) {
        quizService.submitQuestionAttempts(body, quizAttemptId);
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Question attempts submitted successfully",null), HttpStatus.ACCEPTED);
    }

    /*
        this api is used to finish the quiz attempt
        it calculates the score and returns it
     */
    @PostMapping("/attempt/{quizAttemptId}/finish")
    public ResponseEntity<StandardApiResponse<Float>> finishQuizAttempt(@PathVariable Integer quizAttemptId) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt finished and processed successfully",quizService.finishQuizAttempt(quizAttemptId).getScore()), HttpStatus.OK);
    }

    /*
        this api is used to create a new quiz
        it takes the quiz creation dto as a body
        and returns the id of the created quiz
     */
    @PostMapping
    public ResponseEntity<StandardApiResponse<Integer>> createQuiz(@RequestBody QuizCreationDTO body) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz created successfully",quizService.createQuiz(body).getId()), HttpStatus.CREATED);
    }

    /*
        this api is used to get the needed information to
        be able to play the quiz, such as the questions
        and the previous submitted non-finished attempts
     */
    @GetMapping("/attempt/{quizAttemptId}/taking")
    public ResponseEntity<StandardApiResponse<QuizAttemptTakingDTO>> isQuizAttemptTakingPlace(@PathVariable Integer quizAttemptId) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt taking fetched successfully",quizService.getQuizAttemptTaking(quizAttemptId)), HttpStatus.OK);
    }

    /*
        this api is used to get the result of a finished quiz attempt
        it contains the questions, the attempts, the correct answers
     */
    @GetMapping("/attempt/{quizAttemptId}/result")
    public ResponseEntity<StandardApiResponse<QuizAttemptResultDTO>> getQuizAttemptResult(@PathVariable Integer quizAttemptId) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt result fetched successfully",quizService.getQuizAttemptResult(quizAttemptId)), HttpStatus.OK);
    }

    /*
        this api is used to cancel a quiz attempt
        it is used when the user wants to cancel the quiz
        before finishing it
     */
    @DeleteMapping("/attempt/{quizAttemptId}/cancel")
    public ResponseEntity<StandardApiResponse<Void>> cancelQuizAttempt(@PathVariable Integer quizAttemptId) {
        quizService.cancelQuizAttempt(quizAttemptId);
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt canceled successfully"), HttpStatus.OK);
    }


}
