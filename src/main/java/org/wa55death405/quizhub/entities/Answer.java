package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

/*
    * Answer entity represents an answer for a question.
    * Used with types (SHORT_ANSWER, TRUE_FALSE, NUMERIC, FILL_IN_THE_BLANK)

    @Rules
    * Each answer should be associated with a Question
    * The answers are unique for a question

    @Note
    * The answer length is high for the case of fill in the blank questions
 */

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"answer", "question_id"}))
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // TODO : updatable could be changed to false ?

    @Column(nullable = false,length = 1024)
    @NotBlank
    private String answer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    public boolean compareAnswer(String attempt) {
        return this.answer.equalsIgnoreCase(attempt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Answer other)) return false;
        return Objects.equals(id, other.id);
    }
}
