package org.wa55death405.quizhub.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

/*
    * Choice entity represents a choice for a question.
    * Used with types (MULTIPLE_CHOICE, SINGLE_CHOICE)

    @Rules
    * Each choice should be associated with a Question
    * The choices are unique for a question
    * Each choice should have a flag to indicate if it is correct
*/

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"choice", "question_id"})
})
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String choice;
    @Column(nullable = false)
    private Boolean isCorrect;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Choice other = (Choice) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isCorrect,choice);
    }
}
