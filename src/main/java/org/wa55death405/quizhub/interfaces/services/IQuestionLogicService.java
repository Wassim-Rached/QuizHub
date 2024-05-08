package org.wa55death405.quizhub.interfaces.services;

import org.wa55death405.quizhub.entities.QuestionAttempt;

/*
    this interface contains all the methods
    that use logic or processing on the question
    it normally contains sensitive and long algorithms
 */
public interface IQuestionLogicService {
    void handleQuestionAttempt(QuestionAttempt questionAttempt);
}
