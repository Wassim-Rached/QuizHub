package org.wa55death405.quizhub.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderedOptionAttempt {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer position;
    private Boolean isCorrect;

    @ManyToOne
    private OrderedOption orderedOption;
    @ManyToOne
    private QuestionAttempt questionAttempt;

    public boolean validate() {
        boolean is_valid = this.orderedOption.getCorrectPosition().equals(this.position);
        this.isCorrect = is_valid;
        return is_valid;
    }
}
