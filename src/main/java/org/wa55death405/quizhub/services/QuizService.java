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

@Service
@RequiredArgsConstructor
public class QuizService implements IQuizService{
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuestionAttemptRepository questionAttemptRepository;
    private final QuizRepository quizRepository;
    private final IQuizLogicService quizLogicService;
    private final ModelMapper modelMapper;

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

    @Override
    public void submitQuestionAttempts(List<QuestionAttemptSubmissionDTO> questionAttemptTakings, Integer quizAttemptId) {
        if (questionAttemptTakings == null || questionAttemptTakings.isEmpty()) {
            throw new InputValidationException("No question attempts to submit");
        }

        var quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new EntityNotFoundException("Quiz attempt with id " + quizAttemptId + " not found")
        );

        if (quizAttempt.getScore() != null){
            throw new InputValidationException("Quiz attempt already finished");
        }

        List<Question> questions = quizAttempt.getQuiz().getQuestions();
        List<QuestionAttempt> oldQuestionAttemptsWillBeRemoved = new ArrayList<>();
        List<QuestionAttempt> questionAttemptsWillBeSaved = new ArrayList<>();
        List<QuestionAttempt> oldQuestionAttempts = new ArrayList<>(quizAttempt.getQuestionAttempts());
        for (var questionAttemptTaking : questionAttemptTakings) {
            var newQuestionAttempt = questionAttemptTaking.toEntity(quizAttemptId);

            Quiz quiz = quizAttempt.getQuiz();
            if (questions.stream().noneMatch(q -> q.getId().equals(newQuestionAttempt.getQuestion().getId()))){
                throw new InputValidationException("Question with id " + newQuestionAttempt.getQuestion().getId() + " does not belong to quiz with id " + quiz.getId());
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
                throw new InputValidationException("Invalid question response data provided");
            }
        }
    }

    @Override
    public void cancelQuizAttempt(Integer quizAttemptId) {

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
