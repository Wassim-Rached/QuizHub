package org.wa55death405.quizhub.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wa55death405.quizhub.dto.StandardApiResponse;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.enums.StandardApiStatus;
import org.wa55death405.quizhub.interfaces.utils.IFakeDataLogicalGenerator;
import org.wa55death405.quizhub.repositories.QuizAttemptRepository;
import org.wa55death405.quizhub.services.QuizService;

import java.util.List;
import java.util.UUID;

/*
    this class is the controller of the dev
    it contains the apis that are used for development purposes
    and should not be used in production
 */

@RestController
@RequiredArgsConstructor
@Profile("dev")
@RequestMapping("/api/dev")
public class DevController {
    private final QuizService quizService;
    private final QuizAttemptRepository quizAttemptRepository;
    private final IFakeDataLogicalGenerator fakeDataLogicalGenerator;

    /*
        this api is used to submit the perfect score for a quiz attempt
        it takes the quiz attempt id as a path variable
        and submits the perfect score for the quiz attempt
     */
    @PostMapping("/attempt/{quizAttemptId}/submit-perfect")
    @Transactional
    public void submitPerfectResponse(@PathVariable UUID quizAttemptId) {
        var quizAttempt= quizAttemptRepository.findById(quizAttemptId);
        if(quizAttempt.isEmpty()){
            return;
        }
        var quiz = quizAttempt.get().getQuiz();
        List<QuestionAttemptSubmissionDTO> attempts = fakeDataLogicalGenerator.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(quiz);
        quizService.submitQuestionAttempts(attempts,quizAttemptId);
    }

    /*
        this api is used to get the needed information to
        be able to create certain quiz that already exists
        @Param quizId the id of the quiz
        @return the needed information to create the quiz
    */
    @GetMapping("/quiz/{quizId}/creation-info")
    public ResponseEntity<StandardApiResponse<QuizCreationDTO>> getQuizCreationInfo(@PathVariable UUID quizId) {
        return new ResponseEntity<>(new StandardApiResponse<>(StandardApiStatus.SUCCESS, "Quiz creation info fetched successfully", quizService.getQuizCreationInfo(quizId)), HttpStatus.OK);
    }
}
