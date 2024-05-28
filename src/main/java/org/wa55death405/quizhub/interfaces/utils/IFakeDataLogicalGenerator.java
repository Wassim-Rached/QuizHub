package org.wa55death405.quizhub.interfaces.utils;

import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.entities.*;

import java.util.List;

public interface IFakeDataLogicalGenerator {
    QuestionAttemptSubmissionDTO getPerfectScoreQuestionAttemptSubmissionDTO(Question question);

    List<QuestionAttemptSubmissionDTO> getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(Quiz quiz);

    QuestionAttemptSubmissionDTO getRandomQuestionAttemptSubmissionDTO(Question question);

    List<QuestionAttemptSubmissionDTO> getRandomQuestionAttemptSubmissionDTOsForQuiz(Quiz quiz);

    QuizAttempt generate_QuizAttempt(Quiz quiz);
}
