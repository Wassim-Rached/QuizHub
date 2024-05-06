package org.wa55death405.quizhub.interfaces.services;

import org.wa55death405.quizhub.entities.QuestionAttempt;

public interface IQuestionLogicService {
    void handleQuestionAttempt(QuestionAttempt questionAttempt);
}
