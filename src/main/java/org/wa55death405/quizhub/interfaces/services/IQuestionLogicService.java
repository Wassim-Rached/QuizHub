package org.wa55death405.quizhub.interfaces.services;

import org.wa55death405.quizhub.entities.QuestionAttempt;

/*
    this interface contains all the methods
    that use logic or processing on the question
    it normally contains sensitive and long algorithms
 */
public interface IQuestionLogicService {
    /*
        * handle a question attempt
        * by calculating the correctness and all the other needed data
        * like the correct answers and the wrong answers for each question
        * @param questionAttempt the question attempt to handle
     */
    void handleQuestionAttempt(QuestionAttempt questionAttempt);
}
