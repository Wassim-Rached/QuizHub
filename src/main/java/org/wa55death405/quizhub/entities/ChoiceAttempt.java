package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

/*
    * ChoiceAttempt entity represents a choice attempt made by a user for a question.
    * Associated with Choice entity in direct way.

    @Rules
    * Each choice attempt should always be associated with a QuestionAttempt
    * Choice attempts are unique for a question attempt (so the user won't be able to select the same choice twice)
    * Each choice attempt should always be associated with a Choice

    @Note
    * When first created, isCorrect should be null it will be set later
    by the system algorithm (it will just get it from the Choice entity lol)
 */

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"choice_id", "question_attempt_id"})
})
public class ChoiceAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Boolean isCorrect = null;

    @ManyToOne(optional = false)
    @JoinColumn(name = "choice_id")
    private Choice choice;
    @ManyToOne(optional = false)
    @JoinColumn(name = "question_attempt_id")
    private QuestionAttempt questionAttempt;

    @Override
    public int hashCode() {
        return Objects.hash(id, isCorrect);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ChoiceAttempt choiceAttempt)) return false;
        return Objects.equals(id, choiceAttempt.id);
    }
}
