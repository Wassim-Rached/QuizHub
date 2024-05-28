package org.wa55death405.quizhub.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.interfaces.utils.IFakeDataLogicalGenerator;
import org.wa55death405.quizhub.repositories.QuizAttemptRepository;
import org.wa55death405.quizhub.services.QuizService;

import java.util.List;

/*
    this class is the controller of the dev
    it contains the apis that are used for development purposes
    and should not be used in production
 */

@RestController
@RequiredArgsConstructor
@Profile("dev")
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
    public void submitPerfectResponse(@PathVariable Integer quizAttemptId) {
        var quizAttempt= quizAttemptRepository.findById(quizAttemptId);
        if(quizAttempt.isEmpty()){
            return;
        }
        var quiz = quizAttempt.get().getQuiz();
        List<QuestionAttemptSubmissionDTO> attempts = fakeDataLogicalGenerator.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(quiz);
        quizService.submitQuestionAttempts(attempts,quizAttemptId);
    }
}
