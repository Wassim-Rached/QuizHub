package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Check;
import org.wa55death405.quizhub.enums.QuizAccessType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/*
    * Quiz entity represents a quiz.
    * A quiz is most importantly a collection of questions.

    @Rules
    * Each quiz should have a title,
    * Each quiz can have a list of questions or none
    * Each quiz can have a list of attempts or none

    @Note
    * The questions are unique for a quiz
    * The attempts are unique for a quiz
*/

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    @NotBlank
    private String title;
    private QuizAccessType quizAccessType;
    @Check(constraints = "time_limit >= 60")
    private Integer timeLimit;

    // TODO : change the fetch to lazy
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions = new ArrayList<>();
    @OneToMany(mappedBy = "quiz")
    private List<QuizAttempt> attempts = new ArrayList<>();

    public static final int MAX_QUESTION_COUNT = 15;
    public static final int MIN_QUESTION_COUNT = 1;

    public static final int MAX_TIME_LIMIT = 3600;
    public static final int MIN_TIME_LIMIT = 60;
    public static final int GRACE_PERIOD_SECONDS = 20;


    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quiz quiz)) return false;
        return Objects.equals(id, quiz.id);
    }
}
