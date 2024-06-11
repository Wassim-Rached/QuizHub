package org.wa55death405.quizhub.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/*
    * CorrectOptionMatch entity represents a correct match between an option and a match.
    * Associated with Match and Option entities in a direct way.

    @Rules
    * Each correct option match should always be associated with a Question
    * Correct option matches are unique for a question (it will result in unexpected behavior if the same match-option pair is added twice)
    * Each correct option match should always be associated with a Match and an Option

 */

@Entity
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"match_id", "option_id"}))
public class CorrectOptionMatch {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL,optional = false)
    @JoinColumn(name = "match_id")
    private Match match;
    @ManyToOne(cascade = CascadeType.ALL,optional = false)
    @JoinColumn(name = "option_id")
    private Option option;

    @ManyToOne(optional = false)
    private Question question;

    public boolean verifyAttempt(OptionMatchAttempt optionMatchAttempt) {
        var option = optionMatchAttempt.getOption();
        var match = optionMatchAttempt.getMatch();
        return this.option.equals(option) && this.match.equals(match);
    }
}
