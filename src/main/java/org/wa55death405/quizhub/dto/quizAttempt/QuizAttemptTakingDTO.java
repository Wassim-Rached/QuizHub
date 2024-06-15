package org.wa55death405.quizhub.dto.quizAttempt;

import lombok.Data;
import org.wa55death405.quizhub.dto.question.QuestionTakingDTO;
import org.wa55death405.quizhub.dto.quiz.QuizGeneralInfoDTO;
import org.wa55death405.quizhub.entities.QuizAttempt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class QuizAttemptTakingDTO {
    private UUID id;
    private QuizGeneralInfoDTO quiz;
    private List<QuestionTakingDTO> questions;
    private Instant startedAt;

    public QuizAttemptTakingDTO(QuizAttempt quizAttempt) {
        this.id = quizAttempt.getId();
        this.startedAt = quizAttempt.getStartedAt();
        this.quiz = new QuizGeneralInfoDTO(quizAttempt.getQuiz());
        this.questions = quizAttempt.getQuiz().getQuestions().stream().map(q ->{
            var qa = quizAttempt.getQuestionAttempts().stream().filter(qa1 -> qa1.getQuestion().getId().equals(q.getId())).findFirst().orElse(null);
            return new QuestionTakingDTO(q, qa);
        }).collect(Collectors.toList());

        // randomize the questions list order
        Collections.shuffle(this.questions);
    }
}
