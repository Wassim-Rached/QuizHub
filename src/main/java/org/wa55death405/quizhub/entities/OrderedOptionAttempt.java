package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderedOptionAttempt {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private Integer position;
    private Boolean isCorrect = null;

    // TODO : questionAttempt and orderedOption should be unique together
    // TODO : questionAttempt and position should be unique together
    @ManyToOne(optional = false)
    private OrderedOption orderedOption;
    @ManyToOne(optional = false)
    private QuestionAttempt questionAttempt;

    public boolean validate() {
        boolean is_valid = this.orderedOption.getCorrectPosition().equals(this.position);
        this.isCorrect = is_valid;
        return is_valid;
    }
}
