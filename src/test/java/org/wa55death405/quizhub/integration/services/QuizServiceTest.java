package org.wa55death405.quizhub.integration.services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.entities.QuizAttempt;
import org.wa55death405.quizhub.exceptions.IrregularBehaviorException;
import org.wa55death405.quizhub.repositories.QuizAttemptRepository;
import org.wa55death405.quizhub.repositories.QuizRepository;
import org.wa55death405.quizhub.services.QuizService;
import org.wa55death405.quizhub.utils.FakeDataGenerator;
import org.wa55death405.quizhub.utils.FakeDataUtils;

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
            var attemptsSubmissions = FakeDataUtils.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(preExisting__Quiz);

            // when
            quizService.submitQuestionAttempts(attemptsSubmissions, preExisting__QuizAttempt.getId());

            // then
            entityManager.flush();
            entityManager.clear();
            var quizAttempt = quizAttemptRepository.findById(preExisting__QuizAttempt.getId()).orElseThrow(() -> new IrregularBehaviorException("Quiz attempt not found"));
            var questionAttempts = quizAttempt.getQuestionAttempts();
            assertEquals(attemptsSubmissions.size(),questionAttempts.size());
        }
    }

    @Nested
    class Test_cancelQuizAttempt{
        private QuizAttempt preExisting__QuizAttempt;

        @BeforeEach
        void setUp() {
            // Set up pre-existing data, such as a quiz, to be used in multiple tests
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            Quiz preExistingQuizId = quizService.createQuiz(quizCreationDTO);
            preExisting__QuizAttempt = quizService.startQuizAttempt(preExistingQuizId.getId());
        }

        @Test
        void test_normal(){
            // when
            quizService.cancelQuizAttempt(preExisting__QuizAttempt.getId());

            // then
            var quizAttemptIsFound = quizAttemptRepository.findById(preExisting__QuizAttempt.getId());
            assertFalse(quizAttemptIsFound.isPresent());
        }
    }

//    @Nested
//    class Test_getQuizAttemptTaking{
//
//        private QuizAttempt preExisting__QuizAttempt;
//
//        @BeforeEach
//        void setUp() {
//            // Set up pre-existing data, such as a quiz, to be used in multiple tests
//            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
//            FakeDataGenerator.fill(quizCreationDTO);
//            QuizAttempt preExistingQuizId = quizService.createQuiz(quizCreationDTO);
//            preExistingQuizAttemptId = quizService.startQuizAttempt(preExistingQuizId);
//        }
//
//        // normal behavior tests
//        @Test
//        void test_normal() {
//            // when
//            var quizAttemptTaking = quizService.getQuizAttemptTaking(preExistingQuizAttemptId);
//
//            // then
//            assertNotNull(quizAttemptTaking);
//        }
//    }
//
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
            var attemptsSubmissions = FakeDataUtils.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(preExisting__Quiz);
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

//    @Nested
//    class Test_getQuizAttemptResult{
//
//        private Integer preExistingQuizAttemptId;
//
//        @BeforeEach
//        void setUp() {
//            // Set up pre-existing data, such as a quiz, to be used in multiple tests
//            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
//            FakeDataGenerator.fill(quizCreationDTO);
//            Integer preExistingQuizId = quizService.createQuiz(quizCreationDTO);
//            preExistingQuizAttemptId = quizService.startQuizAttempt(preExistingQuizId);
//            quizService.finishQuizAttempt(preExistingQuizAttemptId);
//        }
//
//        // normal behavior tests
//        @Test
//        void test_normal() {
//            // when
//            var quizAttemptResult = quizService.getQuizAttemptResult(preExistingQuizAttemptId);
//
//            // then
//            assertNotNull(quizAttemptResult);
//        }
//    }
//

}