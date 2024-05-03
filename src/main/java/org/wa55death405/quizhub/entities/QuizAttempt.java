package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Float score = null;

    @ManyToOne
    private Quiz quiz;
    @OneToMany(mappedBy = "quizAttempt")
    private List<QuestionAttempt> questionAttempts;

    public static Float calculateScore(QuizAttempt quizAttempt) {
        float score = 0f;
        float totalCoefficients = 0f;
        for (QuestionAttempt questionAttempt : quizAttempt.getQuestionAttempts()) {
            score += questionAttempt.calculateScore();
            totalCoefficients += questionAttempt.getQuestion().getCoefficient();
        }
        if (totalCoefficients == 0) {
            return 0f;
        }
        return score / totalCoefficients;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, score);
    }
}
