package org.wa55death405.quizhub.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.entities.QuestionAttempt;
import org.wa55death405.quizhub.entities.QuizAttempt;
import org.wa55death405.quizhub.interfaces.services.IQuestionLogicService;
import org.wa55death405.quizhub.interfaces.services.IQuizLogicService;
import org.wa55death405.quizhub.repositories.QuizAttemptRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class QuizLogicServiceImpl implements IQuizLogicService {
    private final QuizAttemptRepository quizAttemptRepository;
    private final IQuestionLogicService questionLogicService;

    @Override
    public void processQuizAttempt(QuizAttempt quizAttempt) {
        if (quizAttempt == null) return;

        List<QuestionAttempt> questionAttempts = quizAttempt.getQuestionAttempts();
        for (QuestionAttempt questionAttempt : questionAttempts){
            questionLogicService.handleQuestionAttempt(questionAttempt);
        }

        float score = QuizAttempt.calculateScore(quizAttempt);
        quizAttempt.setScore(score);
        quizAttemptRepository.save(quizAttempt);
    }
}
