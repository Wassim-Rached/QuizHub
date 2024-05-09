package org.wa55death405.quizhub.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptResultDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptTakingDTO;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.exceptions.InputValidationException;
import org.wa55death405.quizhub.interfaces.services.IQuizLogicService;
import org.wa55death405.quizhub.interfaces.services.IQuizService;
import org.wa55death405.quizhub.repositories.*;
import org.wa55death405.quizhub.utils.DBErrorExtractorUtils;

import java.util.ArrayList;
import java.util.List;

/*
    * This class is responsible for providing
    * the api with all the logic it needs
    * that is related to the quiz
*/

@Service
@RequiredArgsConstructor
public class QuizService implements IQuizService{
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuestionAttemptRepository questionAttemptRepository;
    private final QuizRepository quizRepository;
    private final IQuizLogicService quizLogicService;

    @Override
    public Integer startQuizAttempt(Integer quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(
                () -> new EntityNotFoundException("Quiz with id " + quizId + " not found")
        );
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setQuiz(quiz);
        quizAttemptRepository.save(quizAttempt);
        return quizAttempt.getId();
    }

    // TODO : there is a problem with sending new attempts to replace old attempts
    @Override
    public void submitQuestionAttempts(List<QuestionAttemptSubmissionDTO> questionAttemptSubmissions, Integer quizAttemptId) {
        if (questionAttemptSubmissions == null || questionAttemptSubmissions.isEmpty()) {
            System.out.println("No question attempts to submit");
            return;
        }

        var quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new EntityNotFoundException("Quiz attempt with id " + quizAttemptId + " not found")
        );

        if (quizAttempt.isFinished()){
            throw new InputValidationException("Quiz attempt already finished");
        }

        List<Question> questions = quizAttempt.getQuiz().getQuestions();
//        List<QuestionAttempt> oldQuestionAttemptsToBeRemoved = new ArrayList<>();
        List<QuestionAttempt> questionAttemptsWillBeSaved = new ArrayList<>();
        System.out.println("questionAttemptTakings:" + questionAttemptSubmissions.size());
        for (var questionAttemptTaking : questionAttemptSubmissions) {
            boolean isAttemptRelated = questions.stream().anyMatch(q -> q.getId().equals(questionAttemptTaking.getQuestion()));
            if (!isAttemptRelated){
                throw new InputValidationException("Question with id " + questionAttemptTaking.getQuestion() + " does not belong to quiz with id " + quizAttempt.getQuiz().getId());
            }

            QuestionAttempt newQuestionAttempt = questionAttemptTaking.toEntity(quizAttemptId);

            // find if the question attempt is already in the database
            // and put it in a list to be removed
            var oldQuestionAttempt = quizAttempt.getQuestionAttempts().stream()
                    .filter(qa -> qa.getQuestion().getId().equals(questionAttemptTaking.getQuestion()))
                    .findFirst();

            oldQuestionAttempt.ifPresent(questionAttempt -> System.out.println("1-oldQuestionAttempt:" + questionAttempt.getId()));
            oldQuestionAttempt.ifPresent(questionAttemptRepository::delete);
            oldQuestionAttempt.ifPresent(questionAttempt -> System.out.println("2-oldQuestionAttempt:" + questionAttempt.getId()));

            questionAttemptsWillBeSaved.add(newQuestionAttempt);
        }


        try {
            // save new question attempts
            questionAttemptRepository.saveAll(questionAttemptsWillBeSaved);
        }catch (Exception e){
            // this catch is mostly for the case when
            // some id provided in the request is not valid
            var errorDetails = DBErrorExtractorUtils.extractSQLErrorDetails(e.getMessage());
            if (errorDetails.allFieldsPresent()){
                throw new EntityNotFoundException(errorDetails.getTableName() + " with id " + errorDetails.getEntityId() + " not found");
            }else{
                System.out.println(e.getMessage());
                throw new InputValidationException("Invalid question response data provided");
            }
        }
    }

    @Override
    public void cancelQuizAttempt(Integer quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new EntityNotFoundException("Quiz attempt with id " + quizAttemptId + " not found")
        );
        if (quizAttempt.getScore() != null){
            throw new InputValidationException("Quiz attempt already finished");
        }
        quizAttemptRepository.delete(quizAttempt);
    }

    @Override
    public QuizAttemptTakingDTO getQuizAttemptTaking(Integer quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new EntityNotFoundException("Quiz attempt with id " + quizAttemptId + " not found")
        );
        return new QuizAttemptTakingDTO(quizAttempt);
    }

    @Override
    public Float finishQuizAttempt(Integer quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new EntityNotFoundException("Quiz attempt with id " + quizAttemptId + " not found")
        );
        quizLogicService.processQuizAttempt(quizAttempt);
        return quizAttempt.getScore();
    }

    @Override
    public Integer createQuiz(QuizCreationDTO quizCreationDTO) {
        Quiz quiz = quizCreationDTO.toEntity(null);
        quizRepository.save(quiz);
        return quiz.getId();
    }

    @Override
    public QuizAttemptResultDTO getQuizAttemptResult(Integer quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new EntityNotFoundException("Quiz attempt with id " + quizAttemptId + " not found")
        );
        return new QuizAttemptResultDTO(quizAttempt);
    }
}