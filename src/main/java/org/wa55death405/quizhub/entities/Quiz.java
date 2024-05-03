package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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
    private List<Question> questions = List.of();
    @OneToMany(mappedBy = "quiz")
    private List<QuizAttempt> attempts = List.of();

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
