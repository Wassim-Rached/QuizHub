package org.wa55death405.quizhub.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wa55death405.quizhub.dto.StandardPageList;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.dto.StandardApiResponse;
import org.wa55death405.quizhub.dto.quiz.QuizGeneralInfoDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptResultDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptTakingDTO;
import org.wa55death405.quizhub.enums.StandardApiStatus;
import org.wa55death405.quizhub.interfaces.services.IQuizService;

import java.util.List;
import java.util.UUID;

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
        this api is used to get search for quizzes
        it takes the title of the quiz as a query parameter
        along with the page number and the size of the page
        and returns a list of quizzes that match the title
     */
    @GetMapping("/search")
    public ResponseEntity<StandardApiResponse<StandardPageList<QuizGeneralInfoDTO>>> searchQuizzes(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quizzes fetched successfully",quizService.searchQuizzes(title, page, size)), HttpStatus.OK);
    }

    /*
        this api is used to start taking a quiz
        it takes the quiz id as a path variable
        and returns the quiz attempt id
     */
    @PostMapping("/{quizId}/start")
    public ResponseEntity<StandardApiResponse<UUID>> startQuizAttempt(@PathVariable UUID quizId) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt started successfully",quizService.startQuizAttempt(quizId).getId()), HttpStatus.CREATED);
    }

    /*
        this api is used to submit the question
        attempts without processing them
        it takes List of 'QuestionAttemptSubmissionDTO'
        and the quiz attempt id as a path variable
     */
    @PostMapping("/attempt/{quizAttemptId}/submit")
    public ResponseEntity<StandardApiResponse<Void>> submitQuestionAttempts(@RequestBody List<QuestionAttemptSubmissionDTO> body, @PathVariable UUID quizAttemptId) {
        quizService.submitQuestionAttempts(body, quizAttemptId);
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Question attempts submitted successfully",null), HttpStatus.ACCEPTED);
    }

    /*
        this api is used to finish the quiz attempt
        it calculates the score and returns it
     */
    @PostMapping("/attempt/{quizAttemptId}/finish")
    public ResponseEntity<StandardApiResponse<Float>> finishQuizAttempt(@PathVariable UUID quizAttemptId) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt finished and processed successfully",quizService.finishQuizAttempt(quizAttemptId).getScore()), HttpStatus.OK);
    }

    /*
        this api is used to create a new quiz
        it takes the quiz creation dto as a body
        and returns id of the created quiz
     */
    @PostMapping
    public ResponseEntity<StandardApiResponse<UUID>> createQuiz(@RequestBody QuizCreationDTO body) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz created successfully",quizService.createQuiz(body).getId()), HttpStatus.CREATED);
    }

    /*
        this api is used to get the needed information to
        be able to play the quiz, such as the questions
        and the previous submitted non-finished attempts
     */
    @GetMapping("/attempt/{quizAttemptId}/taking")
    public ResponseEntity<StandardApiResponse<QuizAttemptTakingDTO>> getQuizAttemptTaking(@PathVariable UUID quizAttemptId) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt taking fetched successfully",quizService.getQuizAttemptTaking(quizAttemptId)), HttpStatus.OK);
    }

    /*
        this api is used to cancel a quiz attempt
        it is used when the user wants to cancel the quiz
        before finishing it
     */
    @DeleteMapping("/attempt/{quizAttemptId}/cancel")
    public ResponseEntity<StandardApiResponse<Void>> cancelQuizAttempt(@PathVariable UUID quizAttemptId) {
        quizService.cancelQuizAttempt(quizAttemptId);
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt canceled successfully"), HttpStatus.OK);
    }

    /*
        this api is used to get the result of a finished quiz attempt
        it contains the questions, the attempts, the correct answers
     */
    @GetMapping("/attempt/{quizAttemptId}/result")
    public ResponseEntity<StandardApiResponse<QuizAttemptResultDTO>> getQuizAttemptResult(@PathVariable UUID quizAttemptId) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS,"Quiz attempt result fetched successfully",quizService.getQuizAttemptResult(quizAttemptId)), HttpStatus.OK);
    }


}
