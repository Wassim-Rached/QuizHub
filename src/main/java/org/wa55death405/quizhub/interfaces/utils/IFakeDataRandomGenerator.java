package org.wa55death405.quizhub.interfaces.utils;

import org.wa55death405.quizhub.dto.question.QuestionCreationRequestDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.enums.QuestionType;
/*
    this interface contains all the methods
    that generate random fake data for testing
    it normally contains simple and straight forward methods,
    so the generation here is normally based on random generators
    like 'faker' or 'random' or 'math.random' etc.
    NOTE: NO DATABASE OPERATIONS HERE
 */
public interface IFakeDataRandomGenerator {
    /*
        fills the quiz creation DTO with random data;
        by default, it will generate one question for each question type
     */
    void fill(QuizCreationDTO quizCreationDTO);

    /*
        fills the question creation DTO with random data
     */
    void fill(QuestionCreationRequestDTO questionCreationRequestDTO, QuestionType questionType);

    /*
        generates a random quiz with random questions
        by default, it will generate one question for each question type
        @use fill(QuizCreationDTO quizCreationDTO) to fill the quiz creation DTO
     */
    Quiz generate_Quiz();
}
