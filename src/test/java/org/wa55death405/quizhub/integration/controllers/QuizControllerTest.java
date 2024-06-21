package org.wa55death405.quizhub.integration.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.enums.StandardApiStatus;
import org.wa55death405.quizhub.interfaces.utils.IFakeDataLogicalGenerator;
import org.wa55death405.quizhub.interfaces.utils.ISeedDataLoader;
import org.wa55death405.quizhub.repositories.QuizRepository;

import java.util.List;
import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@SpringBootTest()
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets/quiz")
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ISeedDataLoader seedDataLoader;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private IFakeDataLogicalGenerator fakeDataLogicalGenerator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // variables
    private Quiz quiz;
    private UUID quizAttemptId;

    @BeforeAll
    public void setUp() {
        // seed data
        List<QuizCreationDTO> quizCreationDTOS = seedDataLoader.getQuizCreationDTOs();
        for (QuizCreationDTO quizCreationDTO : quizCreationDTOS) {
            Quiz quiz = quizCreationDTO.toEntity(null);
            quizRepository.save(quiz);
        }
        quiz = quizRepository.findAll().get(0);
        Hibernate.initialize(quiz.getQuestions());
    }

    @Test
    @Order(1)
    void searchQuizzes() throws Exception {
        // arrange
        int page = 0;
        int size = 10;

        // act and assert
        this.mockMvc.perform(get("/api/quiz/search?page={page}&size={size}", page, size))
            .andExpect(status().isOk())
            .andDo(document(
                    "search-quizzes",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())
            ));
    }

    @Test
    @Order(2)
    void createQuiz() throws Exception {
        // arrange
        QuizCreationDTO quizCreationDTO = seedDataLoader.getQuizCreationDTOs().get(0);
        var requestBodyJson = this.objectMapper.writeValueAsString(quizCreationDTO);

        // act and assert
        String responseStr = this.mockMvc.perform(post("/api/quiz")
            .contentType("application/json")
            .content(requestBodyJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value(StandardApiStatus.SUCCESS.toString()))
            .andExpect(jsonPath("$.data").exists())
            .andDo(
                document("create-quiz",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())
                )
            ).andReturn().getResponse().getContentAsString();
        // TODO: i dont know what is this even about
        // UUID quizId = UUID.fromString(objectMapper.readTree(responseStr).path("data").asText());
        // quizRepository.deleteById(quizId);
    }

    @Test
    @Order(3)
    void getQuizById() throws Exception {
        // arrange
        UUID quizId = quiz.getId();

        // act and assert
        this.mockMvc.perform(get("/api/quiz/{quizId}", quizId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(StandardApiStatus.SUCCESS.toString()))
            .andExpect(jsonPath("$.data").exists())
            .andDo(document(
                    "get-quiz-by-id",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())
            ));
    }

    @Test
    @Order(4)
    void startQuizAttempt() throws Exception {
        UUID quizId = quiz.getId();
        // act and assert
        String responseStr = this.mockMvc.perform(post("/api/quiz/{quizId}/start", quizId))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value(StandardApiStatus.SUCCESS.toString()))
            .andExpect(jsonPath("$.data").exists())
            .andDo(document(
                    "start-quiz-attempt",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())
            )).andReturn().getResponse().getContentAsString();

        quizAttemptId = UUID.fromString(objectMapper.readTree(responseStr).path("data").asText());
    }

    @Test
    @Order(5)
    void submitQuestionAttempts() throws Exception {
        // arrange
        List<QuestionAttemptSubmissionDTO> attempts = fakeDataLogicalGenerator.getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(quiz);
        var requestJson = this.objectMapper.writeValueAsString(attempts);

        // act and assert
        this.mockMvc.perform(post("/api/quiz/attempt/{quizAttemptId}/submit", quizAttemptId)
                .contentType("application/json")
                .content(requestJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value(StandardApiStatus.SUCCESS.toString()))
                .andDo(document(
                        "submit-question-attempts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @Order(6)
    void getQuizAttemptTaking() throws Exception {
        // arrange, act and assert
        this.mockMvc.perform(get("/api/quiz/attempt/{quizAttemptId}/taking", quizAttemptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StandardApiStatus.SUCCESS.toString()))
                .andExpect(jsonPath("$.data").hasJsonPath())
                .andDo(document(
                        "get-quiz-attempt-taking",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @Order(7)
    @Transactional
    void cancelQuizAttempt() throws Exception {
        // arrange, act and assert
        this.mockMvc.perform(delete("/api/quiz/attempt/{quizAttemptId}/cancel", quizAttemptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StandardApiStatus.SUCCESS.toString()))
                .andDo(document(
                        "cancel-quiz-attempt",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @Order(8)
    void finishQuizAttempt() throws Exception {
        // arrange, act and assert
        this.mockMvc.perform(post("/api/quiz/attempt/{quizAttemptId}/finish", quizAttemptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StandardApiStatus.SUCCESS.toString()))
                .andExpect(jsonPath("$.data").value(100f))
                .andDo(document(
                        "finish-quiz-attempt",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @Order(9)
    void getQuizAttemptResult() throws Exception {
        // arrange, act and assert
        this.mockMvc.perform(get("/api/quiz/attempt/{quizAttemptId}/result", quizAttemptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StandardApiStatus.SUCCESS.toString()))
                .andExpect(jsonPath("$.data").hasJsonPath())
                .andDo(document(
                        "get-quiz-attempt-result",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

}