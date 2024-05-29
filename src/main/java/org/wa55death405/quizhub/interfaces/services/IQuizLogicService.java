package org.wa55death405.quizhub.interfaces.services;

import org.wa55death405.quizhub.entities.QuizAttempt;

/*
    this interface contains all the methods
    that use logic or processing on the quiz
    it normally contains or calls sensitive and long algorithms
 */
public interface IQuizLogicService {
    /*
        * process a quiz attempt
        * by calculating the score and all the other needed data
        * like the correct answers and the wrong answers for each question
        * @param quizAttempt the quiz attempt to process
     */
    void processQuizAttempt(QuizAttempt quizAttempt);
}
