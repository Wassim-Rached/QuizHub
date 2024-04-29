package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Boolean isCorrect;

    @ManyToOne
    private Choice choice;
    @ManyToOne
    private QuestionAttempt questionAttempt;

    @Override
    public int hashCode() {
        return Objects.hash(id, isCorrect);
    }
}
