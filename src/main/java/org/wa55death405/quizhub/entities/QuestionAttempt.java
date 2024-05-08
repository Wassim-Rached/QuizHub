package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"question_id", "quiz_attempt_id"},name = "only_one_question_attempt_per_question_and_quiz_attempt"))
public class QuestionAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Float correctnessPercentage = null;

    @ManyToOne(optional = false)
    private Question question;
    @ManyToOne(optional = false)
    private QuizAttempt quizAttempt;

    // for TRUE_FALSE,SINGLE_CHOICE,SHORT_ANSWER,NUMERIC,FILL_IN_THE_BLANK,
    @OneToOne(mappedBy = "questionAttempt",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private AnswerAttempt answerAttempt;

    // for MULTIPLE_CHOICE, SINGLE_CHOICE
    @OneToMany(mappedBy = "questionAttempt",cascade = CascadeType.ALL)
    private List<ChoiceAttempt> choiceAttempts = new ArrayList<>();

    // for OPTION_ORDERING
    @OneToMany(mappedBy = "questionAttempt",cascade = CascadeType.ALL)
    private List<OrderedOptionAttempt> orderedOptionAttempts = new ArrayList<>();

    // for OPTION_MATCHING
    @OneToMany(mappedBy = "questionAttempt",cascade = CascadeType.ALL)
    private List<OptionMatchAttempt> optionMatchAttempts = new ArrayList<>();

    public Float calculateScore() {
        return correctnessPercentage * question.getCoefficient();
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        QuestionAttempt that = (QuestionAttempt) obj;
        return Objects.equals(id, that.id);
    }

}
