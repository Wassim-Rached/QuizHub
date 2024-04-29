package org.wa55death405.quizhub.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrectOptionMatch {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    @ManyToOne
    private Question question;

    public boolean verifyAttempt(OptionMatchAttempt optionMatchAttempt) {
        var option = optionMatchAttempt.getOption();
        var match = optionMatchAttempt.getMatch();
        return this.option.equals(option) && this.match.equals(match);
    }
}
