package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

/*
    * AnswerAttempt entity represents an answer attempt made by a user for a question.
    * Associated with Answer entity in non-direct way.

    @Rules
    * Each answer attempt should always be associated with a QuestionAttempt

    @Note
    * When first created, isCorrect should be null it will be set later by the system algorithm
*/

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Boolean isCorrect = null;
    @Column(nullable = false)
    @NotBlank
    private String answer;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_attempt_id")
    private QuestionAttempt questionAttempt;

    @Override
    public int hashCode() {
        return Objects.hash(id, isCorrect,answer);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AnswerAttempt answerAttempt)) return false;
        return Objects.equals(id, answerAttempt.id);
    }
}