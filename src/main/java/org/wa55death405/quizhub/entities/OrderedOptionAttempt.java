package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/*
    * OrderedOptionAttempt entity represents an ordered option attempt for a question.
    * Used with types (OPTION_ORDERING)
    * This entity is used to store the user's attempt to order the orderedOptions of a question

    @Rules
    * Each ordered option attempt should be associated with a QuestionAttempt
    * The OrderedOptionAttempt order should be unique for a QuestionAttempt
    * The OrderedOptionAttempt orderedOption should be unique for a QuestionAttempt

    @Note
    * Not to be confused with 'OptionAttempt' entity (which is used with types 'OPTION_MATCHING')
    * When first created, isCorrect should be null it will be set later by the system algorithm
*/

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"position", "question_attempt_id"}),
        @UniqueConstraint(columnNames = {"ordered_option_id", "question_attempt_id"})
})
public class OrderedOptionAttempt {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private Integer position;
    private Boolean isCorrect = null;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ordered_option_id")
    private OrderedOption orderedOption;
    @ManyToOne(optional = false)
    @JoinColumn(name = "question_attempt_id")
    private QuestionAttempt questionAttempt;

    public boolean validate() {
        boolean is_valid = this.orderedOption.getCorrectPosition().equals(this.position);
        this.isCorrect = is_valid;
        return is_valid;
    }
}
