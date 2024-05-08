package org.wa55death405.quizhub.interfaces.services;

import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.quiz.QuizTakingDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptResultDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import java.util.List;

public interface IQuizService {
    Integer createQuiz(QuizCreationDTO quizCreationDTO);
    Integer startQuizAttempt(Integer quizId);
//    QuizTakingDTO getQuizTaking(Integer quizId);
    void submitQuestionAttempts(List<QuestionAttemptSubmissionDTO> questionAttemptTakings, Integer quizAttemptId);
    void cancelQuizAttempt(Integer quizAttemptId);
    //  get quiz attempts with questions but with no corrections during taking the quiz
    Float finishQuizAttempt(Integer quizAttemptId);
    QuizAttemptResultDTO getQuizAttemptResult(Integer quizAttemptId);
}