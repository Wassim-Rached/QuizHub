package org.wa55death405.quizhub.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionMatchAttempt{
    @Id
    @GeneratedValue
    private Integer id;
    private Boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    @ManyToOne
    private QuestionAttempt questionAttempt;
}
