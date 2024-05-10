package org.wa55death405.quizhub.interfaces.services;

import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptResultDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptTakingDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.entities.QuizAttempt;

import java.util.List;

/*
    this interface contains all the methods
    that the api will need related to the quiz
*/

public interface IQuizService {
    Quiz createQuiz(QuizCreationDTO quizCreationDTO);
    QuizAttempt startQuizAttempt(Integer quizId);
    void submitQuestionAttempts(List<QuestionAttemptSubmissionDTO> questionAttemptTakings, Integer quizAttemptId);
    void cancelQuizAttempt(Integer quizAttemptId);
    QuizAttemptTakingDTO getQuizAttemptTaking(Integer quizAttemptId);
    QuizAttempt finishQuizAttempt(Integer quizAttemptId);
    QuizAttemptResultDTO getQuizAttemptResult(Integer quizAttemptId);
}