package org.wa55death405.quizhub.dto.quizAttempt;

import lombok.Data;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptResultDTO;
import org.wa55death405.quizhub.dto.quiz.QuizGeneralInfoDTO;
import org.wa55death405.quizhub.entities.QuizAttempt;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuizAttemptResultDTO {
    private Integer id;
    private Float score;
    private QuizGeneralInfoDTO quiz;
    private List<QuestionAttemptResultDTO> questionAttempts;

    public QuizAttemptResultDTO(QuizAttempt quizAttempt) {
        this.id = quizAttempt.getId();
        this.score = quizAttempt.getScore();
        this.questionAttempts = quizAttempt.getQuestionAttempts().stream().map(QuestionAttemptResultDTO::new).collect(Collectors.toList());
        this.quiz = new QuizGeneralInfoDTO(quizAttempt.getQuiz());
    }
}
