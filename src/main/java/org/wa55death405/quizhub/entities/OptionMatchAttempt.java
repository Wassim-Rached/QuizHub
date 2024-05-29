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
public class OptionMatchAttempt{
    @Id
    @GeneratedValue
    private UUID id;
    private Boolean isCorrect;

    // TODO : match and option should be unique together
    @ManyToOne(optional = false)
    @JoinColumn(name = "match_id")
    private Match match;
    @ManyToOne(optional = false)
    @JoinColumn(name = "option_id")
    private Option option;

    @ManyToOne
    private QuestionAttempt questionAttempt;
}
