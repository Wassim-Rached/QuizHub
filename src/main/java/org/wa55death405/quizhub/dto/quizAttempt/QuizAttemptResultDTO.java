package org.wa55death405.quizhub.dto.quizAttempt;

import lombok.Data;
import org.wa55death405.quizhub.dto.question.QuestionResultDTO;
import org.wa55death405.quizhub.dto.quiz.QuizGeneralInfoDTO;
import org.wa55death405.quizhub.entities.QuizAttempt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class QuizAttemptResultDTO {
    private UUID id;
    private Float score;
    private QuizGeneralInfoDTO quiz;
    private List<QuestionResultDTO> questions;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    public QuizAttemptResultDTO(QuizAttempt quizAttempt) {
        this.id = quizAttempt.getId();
        this.score = quizAttempt.getScore();
        this.startedAt = quizAttempt.getStartedAt();
        this.finishedAt = quizAttempt.getFinishedAt();
        this.questions = quizAttempt.getQuiz().getQuestions().stream().map(q ->{
            var qa = quizAttempt.getQuestionAttempts().stream().filter(qa1 -> qa1.getQuestion().getId().equals(q.getId())).findFirst().orElse(null);
            return new QuestionResultDTO(q, qa);
        }).collect(Collectors.toList());
        this.quiz = new QuizGeneralInfoDTO(quizAttempt.getQuiz());
    }
}
