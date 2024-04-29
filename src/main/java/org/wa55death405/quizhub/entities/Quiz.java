package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;

    @OneToMany(mappedBy = "quiz")
    private Set<Question> questions = Set.of();
    @OneToMany(mappedBy = "quiz")
    private Set<QuizAttempt> attempts = Set.of();

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
