package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

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

    @ManyToOne(optional = false)
    private Quiz quiz;
    @OneToMany(mappedBy = "quizAttempt",cascade = CascadeType.ALL)
    private List<QuestionAttempt> questionAttempts;

    public static Float calculateScore(QuizAttempt quizAttempt) {
        float score = 0f;
        float totalCoefficients = quizAttempt.getQuiz().getQuestions().stream().map(Question::getCoefficient).reduce(0f, Float::sum);
        for (QuestionAttempt questionAttempt : quizAttempt.getQuestionAttempts()) {
            score += questionAttempt.calculateScore();
        }
        if (totalCoefficients == 0) {
            return 0f;
        }
        return (float) Math.round(score / totalCoefficients * 100) / 100;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, score);
    }

    public boolean isFinished() {
        return score != null;
    }
}
