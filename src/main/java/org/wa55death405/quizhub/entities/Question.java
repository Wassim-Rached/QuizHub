package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;
import org.wa55death405.quizhub.enums.QuestionType;

import java.util.Objects;
import java.util.Set;

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String question;
    private Float coefficient = 1f;
    private QuestionType questionType;

    @ManyToOne
    private Quiz quiz;
    @OneToOne(mappedBy = "question")
    private Answer answer;
    @OneToMany(mappedBy = "question")
    private Set<Choice> choices = Set.of();


    @Override
    public int hashCode() {
        return Objects.hash(id, question,coefficient);
    }
}
