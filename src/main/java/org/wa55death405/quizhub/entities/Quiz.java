package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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

    // TODO : change the fetch to lazy
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions = new ArrayList<>();
    @OneToMany(mappedBy = "quiz")
    private List<QuizAttempt> attempts = new ArrayList<>();

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
