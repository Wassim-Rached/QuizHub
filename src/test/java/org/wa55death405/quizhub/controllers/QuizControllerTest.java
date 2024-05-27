package org.wa55death405.quizhub.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.dto.quiz.QuizGeneralInfoDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptResultDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptTakingDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.entities.QuizAttempt;
import org.wa55death405.quizhub.enums.StandardApiStatus;
import org.wa55death405.quizhub.interfaces.services.IQuizService;
import org.wa55death405.quizhub.utils.FakeDataGenerator;
import org.wa55death405.quizhub.utils.FakeDataLogicalUtils;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@WebMvcTest(QuizController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets/quiz")
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IQuizService quizService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void searchQuizzes() throws Exception {
        // arrange
        List<QuizGeneralInfoDTO> expectedQuizzes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            var quiz = Quiz.builder()
                    .id(i)
                    .title("Quiz " + i)
                    .build();
            expectedQuizzes.add(new QuizGeneralInfoDTO(quiz));
        }
        when(quizService.searchQuizzes(anyString())).thenReturn(expectedQuizzes);

        // act and assert
        this.mockMvc.perform(get("/api/quiz/search"))
                .andExpect(status().isOk())
                .andDo(document(
                        "search-quizzes",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void createQuiz() throws Exception {
        // arrange
        QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
        FakeDataGenerator.fill(quizCreationDTO);
        Quiz quiz = quizCreationDTO.toEntity(null);
        quiz.setId(1);
        when(quizService.createQuiz(quizCreationDTO)).thenReturn(quiz);
        var requestBodyJson = this.objectMapper.writeValueAsString(quizCreationDTO);

        // act and assert
        this.mockMvc.perform(post("/api/quiz")
                .contentType("application/json")
                .content(requestBodyJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(StandardApiStatus.SUCCESS.toString()))
                .andExpect(jsonPath("$.data").value(quiz.getId()))
                .andDo(
                        document("create-quiz",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    void startQuizAttempt() throws Exception {
        // arrange
        Integer quizId = 1;
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setId(1);
        when(quizService.startQuizAttempt(quizId)).thenReturn(quizAttempt);

        // act and assert
         this.mockMvc.perform(post("/api/quiz/{quizId}/start", quizId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(StandardApiStatus.SUCCESS.toString()))
                .andExpect(jsonPath("$.data").value(quizAttempt.getId()))
                .andDo(document(
                        "start-quiz-attempt",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void submitQuestionAttempts() throws Exception {
        // arrange
        var quizCreationDTO = new QuizCreationDTO();
        FakeDataGenerator.fill(quizCreationDTO);
        Quiz quiz = quizCreationDTO.toEntity(null);
        var attempts = FakeDataLogicalUtils.getRandomQuestionAttemptSubmissionDTOsForQuiz(quiz);
        var requestJson = this.objectMapper.writeValueAsString(attempts);

        // act and assert
        this.mockMvc.perform(post("/api/quiz/attempt/{quizAttemptId}/submit", 1)
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
    void getQuizAttemptTaking() throws Exception {
        // arrange
        Quiz quiz = FakeDataGenerator.generate_Quiz();
        int index = 0;
        for (var question : quiz.getQuestions()) {
            question.setId(index++);
        }
        var quizAttempt = FakeDataLogicalUtils.generate_QuizAttempt(quiz);
        quizAttempt.setId(1);

        var attempt = new QuizAttemptTakingDTO(quizAttempt);
        when(quizService.getQuizAttemptTaking(quizAttempt.getId())).thenReturn(attempt);

        // act and assert
        this.mockMvc.perform(get("/api/quiz/attempt/{quizAttemptId}/taking", quizAttempt.getId()))
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
    void cancelQuizAttempt() throws Exception {
        // arrange
        Integer quizAttemptId = 1;

        // act and assert
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
    void finishQuizAttempt() throws Exception {
        // arrange
        Integer quizAttemptId = 1;
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setId(quizAttemptId);
        quizAttempt.setScore(50.0f);
        when(quizService.finishQuizAttempt(quizAttemptId)).thenReturn(quizAttempt);

        // act and assert
        this.mockMvc.perform(post("/api/quiz/attempt/{quizAttemptId}/finish", quizAttemptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(StandardApiStatus.SUCCESS.toString()))
                .andExpect(jsonPath("$.data").value(quizAttempt.getScore()))
                .andDo(document(
                        "finish-quiz-attempt",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    void getQuizAttemptResult() throws Exception {
        // arrange
        Quiz quiz = FakeDataGenerator.generate_Quiz();
        int index = 0;
        for (var question : quiz.getQuestions()) {
            question.setId(index++);
        }
        var quizAttempt = FakeDataLogicalUtils.generate_QuizAttempt(quiz);
        quizAttempt.setId(1);

        var attempt = new QuizAttemptResultDTO(quizAttempt);
        when(quizService.getQuizAttemptResult(quizAttempt.getId())).thenReturn(attempt);

        // act and assert
        this.mockMvc.perform(get("/api/quiz/attempt/{quizAttemptId}/result", quizAttempt.getId()))
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