package org.wa55death405.quizhub.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/*
    * OptionMatchAttempt entity represents the attempt of a user to link an option to a match.

    @Rules
    * Each option match attempt should always be associated with a Match, and an Option
    * Match attempts are unique for an [option, match and question attempt]
    (so the user won't be able to select the same option-match pair twice at the same question attempt)

    @Note
    * When first created, isCorrect should be null it will be set later by the system algorithm
*/

@Entity
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"match_id", "option_id", "question_attempt_id"})
        }
)
public class OptionMatchAttempt{
    @Id
    @GeneratedValue
    private UUID id;
    private Boolean isCorrect = null;

    @ManyToOne(optional = false)
    @JoinColumn(name = "match_id")
    private Match match;
    @ManyToOne(optional = false)
    @JoinColumn(name = "option_id")
    private Option option;

    @ManyToOne
    @JoinColumn(name = "question_attempt_id")
    private QuestionAttempt questionAttempt;
}
