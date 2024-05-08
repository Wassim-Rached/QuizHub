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
public class AnswerAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Boolean isCorrect;
    @Column(nullable = false)
    private String answer;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private QuestionAttempt questionAttempt;

    @Override
    public int hashCode() {
        return Objects.hash(id, isCorrect,answer);
    }
}