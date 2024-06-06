package org.wa55death405.quizhub.interfaces.utils;

import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.entities.*;

import java.util.List;

/*
    this interface contains all the methods
    that generate logical fake data for testing
    it normally contains or calls sensitive and long algorithms,
    so the generation here is based on logical algorithms
    NOTE: NO DATABASE OPERATIONS HERE
 */
public interface IFakeDataLogicalGenerator {

    /*
        generates a perfect question attempt submission
        for a given question with all correct answers
     */
    QuestionAttemptSubmissionDTO getPerfectScoreQuestionAttemptSubmissionDTO(Question question);

    /*
        generates a list of perfect question attempt submissions
        for a given quiz with all correct answers
     */
    List<QuestionAttemptSubmissionDTO> getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(Quiz quiz);

    /*
        generates a random question attempt submission
        for a given question with random answers
     */
    QuestionAttemptSubmissionDTO getRandomQuestionAttemptSubmissionDTO(Question question);

    /*
        generates a list of random question attempt submissions
        for a given quiz with random answers
     */
    List<QuestionAttemptSubmissionDTO> getRandomQuestionAttemptSubmissionDTOsForQuiz(Quiz quiz);

    /*
        generates a quiz attempt for a given quiz
        that contains a single attempt for each question
     */
    QuizAttempt generate_QuizAttempt(Quiz quiz);

    /*
        generate a perfect quiz attempt for a given quiz
        that contains a single attempt for each question
        with all correct answers
     */
    QuizAttempt generate_PerfectScoreQuizAttempt(Quiz quiz);
}
