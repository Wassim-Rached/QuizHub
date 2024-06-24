package org.wa55death405.quizhub.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.dto.StandardPageList;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.dto.quiz.QuizGeneralInfoDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptResultDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptTakingDTO;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.enums.QuizAccessType;
import org.wa55death405.quizhub.exceptions.InputValidationException;
import org.wa55death405.quizhub.interfaces.services.IQuizLogicService;
import org.wa55death405.quizhub.interfaces.services.IQuizService;
import org.wa55death405.quizhub.repositories.*;
import org.wa55death405.quizhub.utils.DBErrorExtractorUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    private final EntityManager entityManager;

    @Override
    public StandardPageList<QuizGeneralInfoDTO> searchQuizzes(String title, int page, int size) {
        Page<Quiz> quizzes = quizRepository.findAllByTitleContainingIgnoreCaseAndQuizAccessTypeEquals(title,PageRequest.of(page, size), QuizAccessType.PUBLIC);
        StandardPageList<QuizGeneralInfoDTO> standardPageList = new StandardPageList<>();
        standardPageList.setItems(quizzes.map(QuizGeneralInfoDTO::new).toList());
        standardPageList.setCurrentPage(quizzes.getNumber());
        standardPageList.setCurrentItemsSize(quizzes.getNumberOfElements());
        standardPageList.setTotalItems(quizzes.getTotalElements());
        return standardPageList;
    }

    @Override
    public Quiz createQuiz(QuizCreationDTO quizCreationDTO) {
        Quiz quiz = quizCreationDTO.toEntity(null);
        quizRepository.save(quiz);
        return quiz;
    }

    @Override
    public QuizAttempt startQuizAttempt(UUID quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(
                () -> new EntityNotFoundException("Quiz with id " + quizId + " not found")
        );
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setQuiz(quiz);
        quizAttempt.setStartedAt(Instant.now());
        quizAttemptRepository.save(quizAttempt);
        return quizAttempt;
    }

    @Override
    public QuizAttemptTakingDTO getQuizAttemptTaking(UUID quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new EntityNotFoundException("Quiz attempt with id " + quizAttemptId + " not found")
        );
        if (quizAttempt.isFinished()){
            throw new InputValidationException("Quiz attempt with id " + quizAttemptId + " already finished");
        }
        return new QuizAttemptTakingDTO(quizAttempt);
    }

    @Override
    @Transactional
    public void submitQuestionAttempts(List<QuestionAttemptSubmissionDTO> questionAttemptSubmissions, UUID quizAttemptId) {
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
        for (var questionAttemptTaking : questionAttemptSubmissions) {
            boolean isAttemptRelated = questions.stream().anyMatch(q -> q.getId().equals(questionAttemptTaking.getQuestion()));
            if (!isAttemptRelated){
                throw new InputValidationException("Question with id " + questionAttemptTaking.getQuestion() + " does not belong to quiz with id " + quizAttempt.getQuiz().getId());
            }

            QuestionAttempt newQuestionAttempt = questionAttemptTaking.toEntity(quizAttemptId);

            var oldQuestionAttempt = quizAttempt.getQuestionAttempts().stream()
                    .filter(qa -> qa.getQuestion().getId().equals(questionAttemptTaking.getQuestion()))
                    .findFirst();
            oldQuestionAttempt.ifPresent(qa -> {
                quizAttempt.getQuestionAttempts().remove(qa);
                questionAttemptRepository.delete(qa);
                entityManager.flush();
            });
            quizAttempt.getQuestionAttempts().add(newQuestionAttempt);
        }


        try {
            quizAttemptRepository.save(quizAttempt);
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
    public void cancelQuizAttempt(UUID quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new EntityNotFoundException("Quiz attempt with id " + quizAttemptId + " not found")
        );
        if (quizAttempt.getScore() != null){
            throw new InputValidationException("Quiz attempt already finished");
        }
        quizAttemptRepository.delete(quizAttempt);
    }

    @Override
    public QuizAttempt finishQuizAttempt(UUID quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new EntityNotFoundException("Quiz attempt with id " + quizAttemptId + " not found")
        );
        quizAttempt.setFinishedAt(Instant.now());
        quizLogicService.processQuizAttempt(quizAttempt);
        return quizAttempt;
    }

    @Override
    public QuizAttemptResultDTO getQuizAttemptResult(UUID quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(
                () -> new EntityNotFoundException("Quiz attempt with id " + quizAttemptId + " not found")
        );
        if (!quizAttempt.isFinished()){
            throw new InputValidationException("Quiz attempt with id " + quizAttemptId + " is not finished yet");
        }
        return new QuizAttemptResultDTO(quizAttempt);
    }

    @Override
    public QuizCreationDTO getQuizCreationInfo(UUID quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(
                () -> new EntityNotFoundException("Quiz with id " + quizId + " not found")
        );
        return new QuizCreationDTO(quiz);
    }

    @Override
    public QuizGeneralInfoDTO getQuizById(UUID quizId) {
        Quiz quiz =  quizRepository.findById(quizId).orElseThrow(
                () -> new EntityNotFoundException("Quiz with id " + quizId + " not found")
        );
        return new QuizGeneralInfoDTO(quiz);
    }
}