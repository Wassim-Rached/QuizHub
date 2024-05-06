package org.wa55death405.quizhub.interfaces.services;

import org.wa55death405.quizhub.dto.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.QuizCreationDTO;
import java.util.List;

public interface IQuizService{
    Integer startQuizAttempt(Integer quizId);
    void submitQuestionAttempts(List<QuestionAttemptSubmissionDTO> questionAttemptTakings, Integer quizAttemptId);
    Float finishQuizAttempt(Integer quizAttemptId);
    Integer createQuiz(QuizCreationDTO quizCreationDTO);
}
