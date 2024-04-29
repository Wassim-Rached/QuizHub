package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String answer;

    @OneToOne
    private Question question;

    public boolean compareAnswer(String attempt) {
        return this.answer.equals(attempt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer);
    }
}
