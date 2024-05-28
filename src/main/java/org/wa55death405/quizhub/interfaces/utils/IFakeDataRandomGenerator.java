package org.wa55death405.quizhub.interfaces.utils;

import org.wa55death405.quizhub.dto.question.QuestionCreationRequestDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.enums.QuestionType;

public interface IFakeDataRandomGenerator {
    void fill(QuizCreationDTO quizCreationDTO);

    void fill(QuestionCreationRequestDTO questionCreationRequestDTO, QuestionType questionType);

    Quiz generate_Quiz();
}
