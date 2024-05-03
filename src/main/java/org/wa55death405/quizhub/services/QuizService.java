package org.wa55death405.quizhub.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.dto.*;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.exceptions.InvalidQuestionResponseException;
import org.wa55death405.quizhub.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final QuestionAttemptRepository questionAttemptRepository;
    private final QuestionLogicService questionLogicService;
    private final QuizRepository quizRepository;

    public QuizAttempt processQuizAttempt(QuizAttempt quizAttempt) {
        if (quizAttempt == null) return null;

        List<QuestionAttempt> questionAttempts = quizAttempt.getQuestionAttempts();
        for (QuestionAttempt questionAttempt : questionAttempts){
            questionLogicService.handleQuestionAttempt(questionAttempt);
        }

        float score = QuizAttempt.calculateScore(quizAttempt);
        quizAttempt.setScore(score);
        quizAttemptRepository.save(quizAttempt);

        return quizAttempt;
    }

    public Integer startQuizAttempt(Integer quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(
                () -> new EntityNotFoundException("Quiz with id " + quizId + " not found")
        );
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setQuiz(quiz);
        quizAttemptRepository.save(quizAttempt);
        return quizAttempt.getId();
    }

    public void submitQuestionAttempts(List<QuestionAttemptSubmissionDTO> questionAttemptTakings, Integer quizAttemptId) {
        if (questionAttemptTakings == null || questionAttemptTakings.isEmpty()) {
            throw new InvalidQuestionResponseException("No question attempts to submit");
        }

        var quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new InvalidQuestionResponseException("Quiz attempt with id " + quizAttemptId + " not found")
        );

        List<QuestionAttempt> questionAttempts = new ArrayList<>();
        for (var questionAttemptTaking : questionAttemptTakings) {
            var questionAttempt = questionAttemptTaking.toQuestionAttempt();

            if (questionAttempt.getQuizAttempt() == null || questionAttempt.getQuizAttempt().getId() == null){
                throw new InvalidQuestionResponseException("Quiz attempt id is required");
            }

            // this if statement is comparing two input values
            // sent by the client ,kinda useless...
            // the client can't send other question attempts
            // only those same as the one in the url parameter (perspective of controller)
            if (!questionAttempt.getQuizAttempt().getId().equals(quizAttemptId)){
                throw new InvalidQuestionResponseException("Question attempt with id " + questionAttempt.getId() + " does not belong to quiz attempt with id " + quizAttemptId);
            }

            questionAttempt.setQuizAttempt(quizAttempt);
            questionAttempts.add(questionAttempt);
        }

        questionAttemptRepository.saveAll(questionAttempts);
    }

    public Float finishQuizAttempt(Integer quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new EntityNotFoundException("Quiz attempt with id " + quizAttemptId + " not found")
        );
        quizAttempt = processQuizAttempt(quizAttempt);
        return quizAttempt.getScore();
    }

}
