package org.wa55death405.quizhub.interfaces.services;

import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.quiz.QuizGeneralInfoDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptResultDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptTakingDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.entities.QuizAttempt;

import java.util.List;
import java.util.UUID;

/*
    this interface contains all the methods
    that the api will need related to the quiz
*/

public interface IQuizService {
    List<QuizGeneralInfoDTO> searchQuizzes(String title);

    Quiz createQuiz(QuizCreationDTO quizCreationDTO);

    QuizAttempt startQuizAttempt(UUID quizId);

    QuizAttemptTakingDTO getQuizAttemptTaking(UUID quizAttemptId);

    void submitQuestionAttempts(List<QuestionAttemptSubmissionDTO> questionAttemptTakings, UUID quizAttemptId);

    void cancelQuizAttempt(UUID quizAttemptId);

    QuizAttempt finishQuizAttempt(UUID quizAttemptId);

    QuizAttemptResultDTO getQuizAttemptResult(UUID quizAttemptId);
}