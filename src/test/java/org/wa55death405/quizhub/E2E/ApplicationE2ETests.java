package org.wa55death405.quizhub.E2E;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.enums.StandardApiStatus;
import org.wa55death405.quizhub.interfaces.utils.IFakeDataLogicalGenerator;
import org.wa55death405.quizhub.interfaces.utils.IFakeDataRandomGenerator;
import org.wa55death405.quizhub.repositories.QuizAttemptRepository;
import org.wa55death405.quizhub.repositories.QuizRepository;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApplicationE2ETests {

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
    @Test
    @Transactional
    void testQuizLifecycle() throws JsonProcessingException {
        // Create a quiz
        QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
        fakeDataRandomGenerator.fill(quizCreationDTO);
        String quizCreationRequestBody = this.objectMapper.writeValueAsString(quizCreationDTO);
        Integer predictedQuizId = 1;

        given()
                .contentType("application/json")
                .body(quizCreationRequestBody)
                .when()
                .post(getBaseUrl() + "/quiz")
                .then()
                .statusCode(CREATED.value())
                .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                        "message", notNullValue(),
                        "data", equalTo(predictedQuizId));

        // Search for the quiz
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("title", quizCreationDTO.getTitle());
        given()
                .queryParams(queryParams)
                .when()
                .get(getBaseUrl() + "/quiz/search")
                .then()
                .statusCode(200)
                .body("status", equalTo(StandardApiStatus.SUCCESS.toString()))
                .body("message", notNullValue())
                .body("data",notNullValue())
                .body("data",hasSize(equalTo(1)))
                .body("data[0].id", equalTo(predictedQuizId))
                .body("data[0].title", equalTo(quizCreationDTO.getTitle()));

        // Start an attempt
        var predictedQuizAttemptId = 1;
        given()
                .when()
                .post(getBaseUrl() + "/quiz/" + predictedQuizId + "/start")
                .then()
                .statusCode(CREATED.value())
                .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                        "message", notNullValue(),
                        "data", equalTo(predictedQuizAttemptId));

        // Submit the attempt
        Quiz quiz = quizRepository.findById(predictedQuizId).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
        List<QuestionAttemptSubmissionDTO> questionAttemptSubmissionDTO = fakeDataLogicalGenerator.getRandomQuestionAttemptSubmissionDTOsForQuiz(quiz);
        String questionAttemptSubmissionRequestBody = this.objectMapper.writeValueAsString(questionAttemptSubmissionDTO);

        given()
                .contentType("application/json")
                .body(questionAttemptSubmissionRequestBody)
                .when()
                .post(getBaseUrl() + "/quiz/attempt/" + predictedQuizAttemptId + "/submit")
                .then()
                .statusCode(ACCEPTED.value())
                .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                        "message", notNullValue(),
                        "data", nullValue());

        // Get the attempt taking
        given()
                .when()
                .get(getBaseUrl() + "/quiz/attempt/" + predictedQuizAttemptId + "/taking")
                .then()
                .statusCode(200)
                .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                "message", notNullValue(),
                        "data", notNullValue(),
                        "data.id", equalTo(predictedQuizId),
                        "data.quiz.id", equalTo(predictedQuizId),
                        "data.quiz.title", equalTo(quiz.getTitle()),
                        "data.questions", hasSize(equalTo(quiz.getQuestions().size())),
                        "data.questions", everyItem(anyOf(
                                hasKey("choices"),
                                hasKey("orderedOptions"),
                                hasKey("matches"),
                                hasKey("options")
                        )),
                        "data.questions", everyItem(
                                allOf(
                                        anyOf(
                                                hasEntry(equalTo("choices"), notNullValue()),
                                                hasEntry(equalTo("orderedOptions"), notNullValue()),
                                                hasEntry(equalTo("matches"), notNullValue()),
                                                hasEntry(equalTo("options"), notNullValue())
                                        )
                                )
                        )
                );


        // Submit the attempt again
        var correctQuestionAttemptSubmissionDTO = fakeDataLogicalGenerator.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(quiz);
        var correctQuestionAttemptSubmissionRequestBody = this.objectMapper.writeValueAsString(correctQuestionAttemptSubmissionDTO);

        given()
                .contentType("application/json")
                .body(correctQuestionAttemptSubmissionRequestBody)
                .when()
                .post(getBaseUrl() + "/quiz/attempt/" + predictedQuizAttemptId + "/submit")
                .then()
                .statusCode(ACCEPTED.value())
                .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                        "message", notNullValue(),
                        "data", nullValue());

        // Finish the attempt
        given()
                .when()
                .post(getBaseUrl() + "/quiz/attempt/" + predictedQuizAttemptId + "/finish")
                .then()
                .statusCode(200)
                .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                        "message", notNullValue(),
                        "data", notNullValue(),
                        "data", instanceOf(Float.class),
                        "data", equalTo(100f));

        // Get the attempt result
        given()
                .when()
                .get(getBaseUrl() + "/quiz/attempt/" + predictedQuizAttemptId + "/result")
                .then()
                .statusCode(200)
                .body("status", equalTo(StandardApiStatus.SUCCESS.toString()),
                        "message", notNullValue(),
                        "data", notNullValue(),
                        "data.id", equalTo(predictedQuizId),
                        "data.quiz.id", equalTo(predictedQuizId),
                        "data.quiz.title", equalTo(quiz.getTitle()),
                        "data.questions", hasSize(equalTo(quiz.getQuestions().size())),
                        "data.questions", everyItem(anyOf(
                                hasKey("choices"),
                                hasKey("orderedOptions"),
                                hasKey("matches"),
                                hasKey("options"),
                                hasKey("questionAttempt")
                        )),
                        "data.questions", everyItem(
                                allOf(
                                        anyOf(
                                                hasEntry(equalTo("choices"), notNullValue()),
                                                hasEntry(equalTo("orderedOptions"), notNullValue()),
                                                hasEntry(equalTo("matches"), notNullValue()),
                                                hasEntry(equalTo("options"), notNullValue())
                                        )
                                )
                        ),
                        "data.questions.questionAttempt", notNullValue()
                );
    }

}
