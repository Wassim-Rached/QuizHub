package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String answer;

    @ManyToOne(optional = false)
    private Question question;

    public boolean compareAnswer(String attempt) {
        return this.answer.equalsIgnoreCase(attempt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer);
    }
}
