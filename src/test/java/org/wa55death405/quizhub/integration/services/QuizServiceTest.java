package org.wa55death405.quizhub.integration.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.entities.QuizAttempt;
import org.wa55death405.quizhub.exceptions.InputValidationException;
import org.wa55death405.quizhub.exceptions.IrregularBehaviorException;
import org.wa55death405.quizhub.repositories.QuizAttemptRepository;
import org.wa55death405.quizhub.repositories.QuizRepository;
import org.wa55death405.quizhub.services.QuizService;
import org.wa55death405.quizhub.utils.FakeDataGenerator;
import org.wa55death405.quizhub.utils.FakeDataLogicalUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.config.location=classpath:application-test.yml")
@Transactional // using @Transactional to enable lazy loading of collections
class QuizServiceTest {

    @Autowired
    private QuizService quizService;
    @Autowired
    private QuizAttemptRepository quizAttemptRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private EntityManager entityManager;

    @Nested
    class Test_createQuiz {
        // normal behavior tests
        @Test
        void test_normal() {
            // given
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);

            // when
            Quiz quiz = quizService.createQuiz(quizCreationDTO);

            // then
            assertNotNull(quiz);
            var quizIsFound = quizRepository.findById(quiz.getId());
            assertTrue(quizIsFound.isPresent());
        }

        @Test
        void test_bad_request() {
            // given
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();

            // when
            assertThrows(InputValidationException.class, () -> quizService.createQuiz(quizCreationDTO));
        }
    }

    @Nested
    class Test_startQuizAttempt{
        private Quiz preExisting__Quiz;

        @BeforeEach
        void setUp() {
            // Set up pre-existing data, such as a quiz, to be used in multiple tests
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            preExisting__Quiz = quizService.createQuiz(quizCreationDTO);
        }

        @Test
        void test_normal(){
            // when
            QuizAttempt quizAttempt = quizService.startQuizAttempt(preExisting__Quiz.getId());

            // then
            assertNotNull(quizAttempt);
        }
    }

    @Nested
    class Test_submitQuestionAttempts{
        private QuizAttempt preExisting__QuizAttempt;
        private Quiz preExisting__Quiz;

        @BeforeEach
        void setUp() {
            // generate the quiz
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            preExisting__Quiz = quizService.createQuiz(quizCreationDTO);

            // generate the quiz attempt
            preExisting__QuizAttempt = quizService.startQuizAttempt(preExisting__Quiz.getId());
        }

        @Test
        void test_normal(){
            // given
            var attemptsSubmissions = FakeDataLogicalUtils.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(preExisting__Quiz);

            // when
            quizService.submitQuestionAttempts(attemptsSubmissions, preExisting__QuizAttempt.getId());

            // then
            entityManager.flush();
            entityManager.clear();
            var quizAttempt = quizAttemptRepository.findById(preExisting__QuizAttempt.getId()).orElseThrow(() -> new IrregularBehaviorException("Quiz attempt not found"));
            var questionAttempts = quizAttempt.getQuestionAttempts();
            assertEquals(attemptsSubmissions.size(),questionAttempts.size());
        }

        @Test
        void test_quiz_attempt_already_finished(){
            // given
            quizService.finishQuizAttempt(preExisting__QuizAttempt.getId());
            var attemptsSubmissions = FakeDataLogicalUtils.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(preExisting__Quiz);

            // when
            assertThrows(InputValidationException.class, () -> quizService.submitQuestionAttempts(attemptsSubmissions, preExisting__QuizAttempt.getId()));
        }

        @Test
        void test_quiz_attempt_not_found(){
            // given
            var attemptsSubmissions = FakeDataLogicalUtils.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(preExisting__Quiz);

            // when
            assertThrows(EntityNotFoundException.class, () -> quizService.submitQuestionAttempts(attemptsSubmissions, -1));
        }

        @Test
        void test_unrelated_question_attempt(){
            // given
            var quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            var anotherQuiz = quizService.createQuiz(quizCreationDTO);
            var anotherQuizQuestion = anotherQuiz.getQuestions().get(0);
            var attemptsSubmissions = FakeDataLogicalUtils.getRandomQuestionAttemptSubmissionDTO(anotherQuizQuestion);

            // when
            var ex = assertThrows(InputValidationException.class,
                    () -> quizService.submitQuestionAttempts(List.of(attemptsSubmissions), preExisting__QuizAttempt.getId())
            );
            assertEquals("Question with id " + anotherQuizQuestion.getId() + " does not belong to quiz with id " + preExisting__QuizAttempt.getQuiz().getId(),ex.getMessage());
        }

        @Test
        void test_overlapping_question_attempts(){
            // given
            var attemptsSubmissions_random = FakeDataLogicalUtils.getRandomQuestionAttemptSubmissionDTOsForQuiz(preExisting__Quiz);
            var attemptsSubmissions_perfectScore = FakeDataLogicalUtils.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(preExisting__Quiz);
            quizService.submitQuestionAttempts(attemptsSubmissions_random, preExisting__QuizAttempt.getId());

            // when
            quizService.submitQuestionAttempts(attemptsSubmissions_perfectScore, preExisting__QuizAttempt.getId());
            entityManager.flush();
            entityManager.clear();

            // then
            var quizAttempt = quizService.finishQuizAttempt(preExisting__QuizAttempt.getId());
            assertEquals(100f,quizAttempt.getScore());
        }
    }

    @Nested
    class Test_cancelQuizAttempt{
        private QuizAttempt preExisting__QuizAttempt;

        @BeforeEach
        void setUp() {
            // create the quiz
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            Quiz preExistingQuizId = quizService.createQuiz(quizCreationDTO);
            // start the quiz attempt
            preExisting__QuizAttempt = quizService.startQuizAttempt(preExistingQuizId.getId());
        }

        @Test
        void test_without_attempts(){
            // when
            quizService.cancelQuizAttempt(preExisting__QuizAttempt.getId());

            // then
            var quizAttemptIsFound = quizAttemptRepository.findById(preExisting__QuizAttempt.getId());
            assertFalse(quizAttemptIsFound.isPresent());
        }

        @Test
        void test_with_attempts(){
            //given
            var attempts = FakeDataLogicalUtils.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(preExisting__QuizAttempt.getQuiz());
            quizService.submitQuestionAttempts(attempts, preExisting__QuizAttempt.getId());

            // when
            quizService.cancelQuizAttempt(preExisting__QuizAttempt.getId());

            // then
            var quizAttemptIsFound = quizAttemptRepository.findById(preExisting__QuizAttempt.getId());
            assertFalse(quizAttemptIsFound.isPresent());
        }

    }

    @Nested
    class Test_getQuizAttemptTaking{

        private QuizAttempt preExisting__QuizAttempt;

        @BeforeEach
        void setUp() {
            // Set up pre-existing data, such as a quiz, to be used in multiple tests
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            Quiz preExisting__Quiz = quizService.createQuiz(quizCreationDTO);
            preExisting__QuizAttempt = quizService.startQuizAttempt(preExisting__Quiz.getId());
        }

        // normal behavior tests
        @Test
        void test_normal() {
            // when
            var quizAttemptTaking = quizService.getQuizAttemptTaking(preExisting__QuizAttempt.getId());

            // then
            assertNotNull(quizAttemptTaking);
        }

        @Test
        void test_quiz_attempt_already_finished() {
            // given
            quizService.finishQuizAttempt(preExisting__QuizAttempt.getId());

            // when
            assertThrows(InputValidationException.class, () -> quizService.getQuizAttemptTaking(preExisting__QuizAttempt.getId()));
        }

        @Test
        void test_not_found() {
            // when
            assertThrows(EntityNotFoundException.class, () -> quizService.getQuizAttemptTaking(-1));
        }
    }

    @Nested
    class Test_finishQuizAttempt{
        private QuizAttempt preExisting__QuizAttempt;

        @BeforeEach
        void setUp() {
            // generate the quiz
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            var preExisting__Quiz = quizService.createQuiz(quizCreationDTO);

            // generate the quiz attempt
            preExisting__QuizAttempt = quizService.startQuizAttempt(preExisting__Quiz.getId());

            // submit the quiz attempt
            var attemptsSubmissions = FakeDataLogicalUtils.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(preExisting__Quiz);
            quizService.submitQuestionAttempts(attemptsSubmissions, preExisting__QuizAttempt.getId());
            entityManager.flush();
            entityManager.clear();
        }

        @Test
        void test_normal(){
            // given

            // when
            QuizAttempt quizAttempt = quizService.finishQuizAttempt(preExisting__QuizAttempt.getId());

            // then
            assertNotNull(quizAttempt);
            // TODO : dumb ass algorithm got one question wrong
            assertEquals(100f,quizAttempt.getScore());
        }

    }

    @Nested
    class Test_getQuizAttemptResult{
        private QuizAttempt preExisting__QuizAttempt;
        private Quiz preExisting__Quiz;

        @BeforeEach
        void setUp() {
            // Set up pre-existing data, such as a quiz, to be used in multiple tests
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            preExisting__Quiz = quizService.createQuiz(quizCreationDTO);
            preExisting__QuizAttempt = quizService.startQuizAttempt(preExisting__Quiz.getId());
        }

        // normal behavior tests
        @Test
        void test_with_attempts() {
            // given
            var attempts = FakeDataLogicalUtils.getRandomQuestionAttemptSubmissionDTOsForQuiz(preExisting__Quiz);
            quizService.submitQuestionAttempts(attempts,preExisting__QuizAttempt.getId());
            quizService.finishQuizAttempt(preExisting__QuizAttempt.getId());
            entityManager.flush();
            entityManager.clear();

            // when
            var quizAttemptResult = quizService.getQuizAttemptResult(preExisting__QuizAttempt.getId());

            // then
            assertNotNull(quizAttemptResult);
            assertNotNull(quizAttemptResult.getScore());
            assertTrue(quizAttemptResult.getQuestions().stream().noneMatch(q-> q.getQuestionAttempt() == null));
            assertEquals(preExisting__Quiz.getQuestions().size(),quizAttemptResult.getQuestions().size());
        }

        @Test
        void test_without_attempts() {
            // given
            quizService.finishQuizAttempt(preExisting__QuizAttempt.getId());
            entityManager.flush();
            entityManager.clear();

            // when
            var quizAttemptResult = quizService.getQuizAttemptResult(preExisting__QuizAttempt.getId());

            // then
            assertNotNull(quizAttemptResult);
            assertEquals(0f,quizAttemptResult.getScore());
            assertTrue(quizAttemptResult.getQuestions().stream().noneMatch(q-> q.getQuestionAttempt() != null));
            assertEquals(preExisting__Quiz.getQuestions().size(),quizAttemptResult.getQuestions().size());
        }

        @Test
        void test_not_found() {
            // when
            assertThrows(EntityNotFoundException.class, () -> quizService.getQuizAttemptResult(-1));
        }

        @Test
        void test_quiz_attempt_not_finished() {
            // when
            var ex = assertThrows(InputValidationException.class, () -> quizService.getQuizAttemptResult(preExisting__QuizAttempt.getId()));
            assertEquals("Quiz attempt with id " + preExisting__QuizAttempt.getId() + " is not finished yet",ex.getMessage());
        }
    }


}