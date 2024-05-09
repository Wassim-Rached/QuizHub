package org.wa55death405.quizhub.integration.services;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.entities.Quiz;
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

    @Nested
    class Test_createQuiz {
        // normal behavior tests
        @Test
        void test_normal() {
            // given
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);

            // when
            Integer quizId = quizService.createQuiz(quizCreationDTO);

            // then
            assertNotNull(quizId);
            var quizIsFound = quizRepository.findById(quizId);
            assertTrue(quizIsFound.isPresent());
        }
    }

    @Nested
    class Test_startQuizAttempt{
        private Integer preExistingQuizId;

        @BeforeEach
        void setUp() {
            // Set up pre-existing data, such as a quiz, to be used in multiple tests
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            preExistingQuizId = quizService.createQuiz(quizCreationDTO);
        }

        @Test
        void test_normal(){
            // when
            Integer quizAttemptId = quizService.startQuizAttempt(preExistingQuizId);

            // then
            assertNotNull(quizAttemptId);
        }
    }

    @Nested
    class Test_submitQuestionAttempts{

        private Integer preExistingQuizAttemptId;
        private Quiz preExistingQuiz;

        @BeforeEach
        void setUp() {
            // Set up pre-existing data, such as a quiz, to be used in multiple tests
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            Integer preExistingQuizId = quizService.createQuiz(quizCreationDTO);
            preExistingQuiz = quizRepository.findById(preExistingQuizId).orElseThrow(() -> new IrregularBehaviorException("Quiz not found"));
            preExistingQuizAttemptId = quizService.startQuizAttempt(preExistingQuizId);
        }

        @Test
            void test_normal(){
            // given
            var attempts = FakeDataUtils.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(preExistingQuiz);

            // when
            quizService.submitQuestionAttempts(attempts, preExistingQuizAttemptId);

            // then
            var questionAttempts = quizAttemptRepository.findById(preExistingQuizAttemptId).orElseThrow(() -> new IrregularBehaviorException("Quiz attempt not found")).getQuestionAttempts();
            assertEquals(preExistingQuiz.getQuestions().size(), questionAttempts.size());
        }
    }

    @Nested
    class Test_cancelQuizAttempt{
        private Integer preExistingQuizAttemptId;

        @BeforeEach
        void setUp() {
            // Set up pre-existing data, such as a quiz, to be used in multiple tests
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            Integer preExistingQuizId = quizService.createQuiz(quizCreationDTO);
            preExistingQuizAttemptId = quizService.startQuizAttempt(preExistingQuizId);
        }

        @Test
        void test_normal(){
            // when
            quizService.cancelQuizAttempt(preExistingQuizAttemptId);

            // then
            var quizAttemptIsFound = quizAttemptRepository.findById(preExistingQuizAttemptId);
            assertFalse(quizAttemptIsFound.isPresent());
        }
    }

    @Nested
    class Test_getQuizAttemptTaking{

        private static Integer preExistingQuizAttemptId;

        @BeforeEach
        void setUp() {
            // Set up pre-existing data, such as a quiz, to be used in multiple tests
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            Integer preExistingQuizId = quizService.createQuiz(quizCreationDTO);
            preExistingQuizAttemptId = quizService.startQuizAttempt(preExistingQuizId);
        }

        // normal behavior tests
        @Test
        void test_normal() {
            // when
            var quizAttemptTaking = quizService.getQuizAttemptTaking(preExistingQuizAttemptId);

            // then
            assertNotNull(quizAttemptTaking);
        }
    }

    @Nested
    class Test_finishQuizAttempt{

        private Integer preExistingQuizAttemptId;

        @BeforeEach
        void setUp() {
            // Set up pre-existing data, such as a quiz, to be used in multiple tests
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            Integer preExistingQuizId = quizService.createQuiz(quizCreationDTO);
            preExistingQuizAttemptId = quizService.startQuizAttempt(preExistingQuizId);
        }

        // normal behavior tests
        @Test
        void test_normal() {
            // when
            Float score = quizService.finishQuizAttempt(preExistingQuizAttemptId);

            // then
            assertEquals(0f, score);
        }
    }

    @Nested
    class Test_getQuizAttemptResult{

        private Integer preExistingQuizAttemptId;

        @BeforeEach
        void setUp() {
            // Set up pre-existing data, such as a quiz, to be used in multiple tests
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            FakeDataGenerator.fill(quizCreationDTO);
            Integer preExistingQuizId = quizService.createQuiz(quizCreationDTO);
            preExistingQuizAttemptId = quizService.startQuizAttempt(preExistingQuizId);
            quizService.finishQuizAttempt(preExistingQuizAttemptId);
        }

        // normal behavior tests
        @Test
        void test_normal() {
            // when
            var quizAttemptResult = quizService.getQuizAttemptResult(preExistingQuizAttemptId);

            // then
            assertNotNull(quizAttemptResult);
        }
    }


}