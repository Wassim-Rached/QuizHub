package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/*
    * QuizAttempt entity represents a quiz attempt made by a user.
    * A quiz attempt is a collection of question attempts.

    @Rules
    * Each quiz attempt should be associated with a Quiz

    @Note
    * When first created, score should be null it will be set later by the system algorithm
*/

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Check(constraints = "started_at <= finished_at")
@Check(constraints = "score >= 0 AND score <= 100")
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Float score = null;
    @Column(nullable = false)
    private Instant startedAt;
    private Instant finishedAt;

    @ManyToOne(optional = false)
    private Quiz quiz;
    // TODO : change the fetch to lazy
    @OneToMany(mappedBy = "quizAttempt",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<QuestionAttempt> questionAttempts = new ArrayList<>();

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

    public boolean isFinishedInTime() {
        if (quiz.getTimeLimit() == null) return true;
        return finishedAt.isBefore( startedAt.plusSeconds( quiz.getTimeLimit() + Quiz.GRACE_PERIOD_SECONDS ) );
    }
}
