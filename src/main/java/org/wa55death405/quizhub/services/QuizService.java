package org.wa55death405.quizhub.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.dto.*;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.exceptions.InvalidQuestionResponseException;
import org.wa55death405.quizhub.repositories.*;
import org.wa55death405.quizhub.utils.DBErrorExtractorUtils;

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
                () -> new EntityNotFoundException("Quiz attempt with id " + quizAttemptId + " not found")
        );

        if (quizAttempt.getScore() != null){
            throw new InvalidQuestionResponseException("Quiz attempt already finished");
        }

        List<QuestionAttempt> oldQuestionAttemptsWillBeRemoved = new ArrayList<>();
        List<QuestionAttempt> questionAttemptsWillBeSaved = new ArrayList<>();
        List<QuestionAttempt> oldQuestionAttempts = new ArrayList<>(quizAttempt.getQuestionAttempts());
        for (var questionAttemptTaking : questionAttemptTakings) {
            var newQuestionAttempt = questionAttemptTaking.toQuestionAttempt(quizAttemptId);

            if (newQuestionAttempt.getQuizAttempt() == null || newQuestionAttempt.getQuizAttempt().getId() == null){
                throw new InvalidQuestionResponseException("Quiz attempt id is required");
            }

            // find if the question attempt is already in the database
            // and put it in a list to be removed
            var oldQuestionAttempt = oldQuestionAttempts.stream().filter(oqa -> oqa.getQuestion().getId().equals(newQuestionAttempt.getQuestion().getId())).findFirst();
            oldQuestionAttempt.ifPresent(oldQuestionAttemptsWillBeRemoved::add);

            newQuestionAttempt.setQuizAttempt(quizAttempt);
            questionAttemptsWillBeSaved.add(newQuestionAttempt);
        }

        // remove old question attempts
        questionAttemptRepository.deleteAll(oldQuestionAttemptsWillBeRemoved);

        try {
            // save new question attempts
            questionAttemptRepository.saveAll(questionAttemptsWillBeSaved);
        }catch (Exception e){
            var errorDetails = DBErrorExtractorUtils.extractSQLErrorDetails(e.getMessage());
            if (errorDetails.allFieldsPresent()){
                throw new EntityNotFoundException(errorDetails.getTableName() + " with id " + errorDetails.getEntityId() + " not found");
            }else{
                throw new InvalidQuestionResponseException("Invalid question response data provided");
            }

        }


    }

    public Float finishQuizAttempt(Integer quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new EntityNotFoundException("Quiz attempt with id " + quizAttemptId + " not found")
        );
        quizAttempt = processQuizAttempt(quizAttempt);
        return quizAttempt.getScore();
    }

}
