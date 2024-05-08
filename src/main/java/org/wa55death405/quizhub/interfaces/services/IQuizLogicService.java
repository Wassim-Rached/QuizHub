package org.wa55death405.quizhub.interfaces.services;

import org.wa55death405.quizhub.entities.QuizAttempt;

/*
    this interface contains all the methods
    that use logic or processing on the quiz
    it normally contains or calls sensitive and long algorithms
 */
public interface IQuizLogicService {
    void processQuizAttempt(QuizAttempt quizAttempt);
}
