package org.wa55death405.quizhub.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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
