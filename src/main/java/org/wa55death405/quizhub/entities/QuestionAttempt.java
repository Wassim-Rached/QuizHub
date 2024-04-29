package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    private QuizAttempt quizAttempt;

    // for TRUE_FALSE,SINGLE_CHOICE,SHORT_ANSWER,NUMERIC,FILL_IN_THE_BLANK,
    @OneToOne(mappedBy = "questionAttempt",fetch = FetchType.LAZY)
    private AnswerAttempt answerAttempt;

    // for MULTIPLE_CHOICE, SINGLE_CHOICE
    @OneToMany(mappedBy = "questionAttempt")
    private Set<ChoiceAttempt> choiceAttempts = Set.of();

    // for OPTION_ORDERING
    @OneToMany(mappedBy = "questionAttempt")
    private Set<OrderedOptionAttempt> orderedOptionAttempts = Set.of();

    // for OPTION_MATCHING
    @OneToMany(mappedBy = "questionAttempt")
    private Set<OptionMatchAttempt> optionMatchAttempts = Set.of();

    public Float calculateScore() {
        return correctnessPercentage * question.getCoefficient();
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, correctnessPercentage);
    }

}
