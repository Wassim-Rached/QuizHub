package org.wa55death405.quizhub.e2e;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.enums.QuestionType;
import org.wa55death405.quizhub.enums.StandardApiStatus;
import org.wa55death405.quizhub.interfaces.utils.IFakeDataLogicalGenerator;
import org.wa55death405.quizhub.interfaces.utils.IFakeDataRandomGenerator;
import org.wa55death405.quizhub.repositories.QuizRepository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApplicationE2ETests {

    @Autowired
    private IFakeDataRandomGenerator fakeDataRandomGenerator;
    @Autowired
    private IFakeDataLogicalGenerator fakeDataLogicalGenerator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private QuizRepository quizRepository;

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api";
    }

    /*
        * This test method covers the complete lifecycle of a quiz attempt:
        * 1. Create a quiz.
        * 2. Search for the created quiz.
        * 3. Start an attempt for the quiz.
        * 4. Submit the quiz attempt.
        * 5. Retrieve the attempt taking details.
        * 6. Resubmit the attempt (if applicable).
        * 7. Finish the attempt.
        * 8. Retrieve the attempt result.
    */
    // TODO: validation with json schema might be needed
    @Nested
    @TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class TestQuizLifecycle{
        private Quiz quiz;
        private UUID quizAttemptId;

        // create quiz
        @Test
        @Order(1)
        void testCreateQuiz() throws JsonProcessingException {
            // Create a quiz
            QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
            fakeDataRandomGenerator.fill(quizCreationDTO);
            this.quiz = quizCreationDTO.toEntity(null);
            String quizCreationRequestBody = objectMapper.writeValueAsString(quizCreationDTO);

            var response = given()
                    .contentType("application/json")
                    .body(quizCreationRequestBody)
                    .when()
                    .post(getBaseUrl() + "/quiz")
                    .then()
                    .statusCode(CREATED.value())
                    .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                            "message", notNullValue(),
                            "data", matchesPattern("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"))
                    .extract()
                    .response();
            UUID quizId = UUID.fromString(response.jsonPath().getString("data"));
            quiz = quizRepository.findById(quizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        }

        // search for the quiz
        @Test
        @Order(2)
        void testSearchQuiz() {
            // Search for the quiz
            HashMap<String, String> queryParams = new HashMap<>();
            queryParams.put("title", quiz.getTitle().substring(1, 3));
            queryParams.put("page", "0");
            queryParams.put("size", "10");
            given()
                    .queryParams(queryParams)
                    .when()
                    .get(getBaseUrl() + "/quiz/search")
                    .then()
                    .statusCode(200)
                    .body("status", equalTo(StandardApiStatus.SUCCESS.toString()))
                    .body("message", notNullValue())
                    .body("data", notNullValue())
                    .body("data.currentPage", equalTo(0))
                    .body("data.currentItemsSize", equalTo(1))
                    .body("data.totalItems", equalTo(1))
                    .body("data.items", hasSize(equalTo(1)))
                    .body("data.items[0].id", equalTo(this.quiz.getId().toString()))
                    .body("data.items[0].title", equalTo(this.quiz.getTitle()));
        }

        @Test
        @Order(3)
        void testStartQuizAttempt() {
            // Start an attempt
            var startQuizResponse = given()
                    .when()
                    .post(getBaseUrl() + "/quiz/" + this.quiz.getId() + "/start")
                    .then()
                    .statusCode(CREATED.value())
                    .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                            "message", notNullValue(),
                            "data", matchesPattern("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"))
                    .extract()
                    .response();

            this.quizAttemptId = UUID.fromString(startQuizResponse.jsonPath().getString("data"));
        }

        // Submit the attempt
        @Test
        @Order(4)
        void testSubmitQuizAttempt() throws JsonProcessingException {
            List<QuestionAttemptSubmissionDTO> questionAttemptSubmissionDTO = fakeDataLogicalGenerator.getRandomQuestionAttemptSubmissionDTOsForQuiz(this.quiz);
            String questionAttemptSubmissionRequestBody = objectMapper.writeValueAsString(questionAttemptSubmissionDTO);

            given()
                    .contentType("application/json")
                    .body(questionAttemptSubmissionRequestBody)
                    .when()
                    .post(getBaseUrl() + "/quiz/attempt/" + quizAttemptId + "/submit")
                    .then()
                    .statusCode(ACCEPTED.value())
                    .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                            "message", notNullValue(),
                            "data", nullValue());
        }

        // Get the attempt taking
        @Test
        @Order(5)
        void testGetQuizAttemptTaking() {
            given()
                .when()
                .get(getBaseUrl() + "/quiz/attempt/" + quizAttemptId + "/taking")
                .then()
                .statusCode(200)
                .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                    "message", notNullValue(),
                    "data", notNullValue(),
                    "data.id", equalTo(quizAttemptId.toString()),
                    "data.quiz.id", equalTo(quiz.getId().toString()),
                    "data.quiz.title", equalTo(quiz.getTitle()),
                    "data.questions", hasSize(equalTo(quiz.getQuestions().size())),
                    "data.questions", everyItem(
                        allOf(
                            // Ensure "questionAttempt" is always non-null
                            hasEntry(equalTo("questionAttempt"), notNullValue()),
                            hasEntry(equalTo("question"), notNullValue()),
                            hasEntry(equalTo("coefficient"), notNullValue()),
                            hasEntry(equalTo("questionType"), notNullValue()),
                            // Ensure structure for each question type
                            anyOf(
                                // for SINGLE_CHOICE and MULTIPLE_CHOICE
                                allOf(
                                    hasEntry(equalTo("choices"), notNullValue()),
                                    hasEntry(equalTo("orderedOptions"), nullValue()),
                                    hasEntry(equalTo("matches"), nullValue()),
                                    hasEntry(equalTo("options"), nullValue())
                                ),
                                // for OPTION_ORDERING
                                allOf(
                                    hasEntry(equalTo("choices"), nullValue()),
                                    hasEntry(equalTo("orderedOptions"), notNullValue()),
                                    hasEntry(equalTo("matches"), nullValue()),
                                    hasEntry(equalTo("options"), nullValue())
                                ),
                                // for OPTION_MATCHING
                                allOf(
                                    hasEntry(equalTo("choices"), nullValue()),
                                    hasEntry(equalTo("orderedOptions"), nullValue()),
                                    hasEntry(equalTo("matches"), notNullValue()),
                                    hasEntry(equalTo("options"), notNullValue())
                                ),
                                // for SHORT_ANSWER, TRUE_FALSE, FILL_IN_THE_BLANK, NUMERIC
                                allOf(
                                    hasEntry(equalTo("choices"), nullValue()),
                                    hasEntry(equalTo("orderedOptions"), nullValue()),
                                    hasEntry(equalTo("matches"), nullValue()),
                                    hasEntry(equalTo("options"), nullValue()),
                                    anyOf(
                                        hasEntry(equalTo("questionType"), equalTo(QuestionType.SHORT_ANSWER.getValue())),
                                        hasEntry(equalTo("questionType"), equalTo(QuestionType.TRUE_FALSE.getValue())),
                                        hasEntry(equalTo("questionType"), equalTo(QuestionType.FILL_IN_THE_BLANK.getValue())),
                                        hasEntry(equalTo("questionType"), equalTo(QuestionType.NUMERIC.getValue()))
                                    )
                                )
                            )
                        )
                ));
        }

        // Resubmit the attempt
        @Test
        @Order(4)
        void testReSubmitQuizAttempt() throws JsonProcessingException {
            this.quiz = quizRepository.findById(this.quiz.getId()).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
            List<QuestionAttemptSubmissionDTO> questionAttemptSubmissionDTO = fakeDataLogicalGenerator.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(this.quiz);
            String questionAttemptSubmissionRequestBody = objectMapper.writeValueAsString(questionAttemptSubmissionDTO);

            given()
                    .contentType("application/json")
                    .body(questionAttemptSubmissionRequestBody)
                    .when()
                    .post(getBaseUrl() + "/quiz/attempt/" + quizAttemptId + "/submit")
                    .then()
                    .statusCode(ACCEPTED.value())
                    .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                    "message", notNullValue(),
                            "data", nullValue());
        }

        // Finish the attempt
        @Test
        @Order(6)
        void testFinishQuizAttempt() {
            given()
                .when()
                .post(getBaseUrl() + "/quiz/attempt/" + quizAttemptId + "/finish")
                .then()
                .statusCode(200)
                .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                        "message", notNullValue(),
                        "data", notNullValue(),
                        "data", instanceOf(Float.class),
                        "data", equalTo(100f));
        }

        // Get the attempt result
        @Test
        @Order(7)
        void testGetQuizAttemptResult() {
            given()
                    .when()
                    .get(getBaseUrl() + "/quiz/attempt/" + quizAttemptId + "/result")
                    .then()
                    .statusCode(200)
                    .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                    "message", notNullValue(),
                            "data", notNullValue(),
                            "data.id", equalTo(this.quizAttemptId.toString()),
                            "data.quiz.id", equalTo(this.quiz.getId().toString()),
                            "data.quiz.title", equalTo(this.quiz.getTitle()),
                            "data.questions", hasSize(equalTo(this.quiz.getQuestions().size())),
                            "data.questions", everyItem(
                                allOf(
                                    // Ensure "questionAttempt" is always non-null
                                    hasEntry(equalTo("questionAttempt"), notNullValue()),
                                    hasEntry(equalTo("question"), notNullValue()),
                                    hasEntry(equalTo("coefficient"), notNullValue()),
                                    hasEntry(equalTo("questionType"), notNullValue()),
                                    // Ensure structure for each question type
                                    anyOf(
                                        // for SINGLE_CHOICE and MULTIPLE_CHOICE
                                        allOf(
                                            hasEntry(equalTo("choices"), notNullValue()),
                                            hasEntry(equalTo("orderedOptions"), nullValue()),
                                            hasEntry(equalTo("matches"), nullValue()),
                                            hasEntry(equalTo("options"), nullValue())
                                        ),
                                        // for OPTION_ORDERING
                                        allOf(
                                            hasEntry(equalTo("choices"), nullValue()),
                                            hasEntry(equalTo("orderedOptions"), notNullValue()),
                                            hasEntry(equalTo("matches"), nullValue()),
                                            hasEntry(equalTo("options"), nullValue())
                                        ),
                                        // for OPTION_MATCHING
                                        allOf(
                                            hasEntry(equalTo("choices"), nullValue()),
                                            hasEntry(equalTo("orderedOptions"), nullValue()),
                                            hasEntry(equalTo("matches"), notNullValue()),
                                            hasEntry(equalTo("options"), notNullValue())
                                        ),
                                        // for SHORT_ANSWER, TRUE_FALSE, FILL_IN_THE_BLANK, NUMERIC
                                        allOf(
                                            hasEntry(equalTo("choices"), nullValue()),
                                            hasEntry(equalTo("orderedOptions"), nullValue()),
                                            hasEntry(equalTo("matches"), nullValue()),
                                            hasEntry(equalTo("options"), nullValue()),
                                            anyOf(
                                                hasEntry(equalTo("questionType"), equalTo(QuestionType.SHORT_ANSWER.getValue())),
                                                hasEntry(equalTo("questionType"), equalTo(QuestionType.TRUE_FALSE.getValue())),
                                                hasEntry(equalTo("questionType"), equalTo(QuestionType.FILL_IN_THE_BLANK.getValue())),
                                                hasEntry(equalTo("questionType"), equalTo(QuestionType.NUMERIC.getValue()))
                                            )
                                        )
                                    )
                    )));
        }
    }

}
