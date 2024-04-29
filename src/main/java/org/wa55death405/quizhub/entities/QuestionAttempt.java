package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Float correctnessPercentage = null;

    @ManyToOne
    private Question question;
    @OneToOne(mappedBy = "questionAttempt",fetch = FetchType.LAZY)
    private AnswerAttempt answerAttempt;
    @ManyToOne
    private QuizAttempt quizAttempt;
    @OneToMany
    private Set<ChoiceAttempt> choiceAttempts = Set.of();

    public Float calculateScore() {
        return correctnessPercentage * question.getCoefficient();
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, correctnessPercentage);
    }

}
