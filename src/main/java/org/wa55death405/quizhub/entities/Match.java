package org.wa55death405.quizhub.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Comparator;
import java.util.UUID;

/*
    * Match entity represents a match for a question.
    * Used with the type (OPTION_MATCHING)

    @Rules
    * Each match should be associated with a Question
    * The matches are unique for a question
    * Each match should have a flag to indicate if it is correct

    @Note
    * the match is the thing on the right (to not confuse with the 'option')
    * the match gets a bigger length because it might be a definition or something
 */

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"match", "question_id"})
})
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, length = 1024)
    @NotBlank
    private String match;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    public static Comparator<Match> matchComparator = Comparator.comparing(Match::getMatch);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Match comparedMatch = (Match) obj;
        if (this.id !=null) return id.equals(comparedMatch.id);

        if (this.question != null && comparedMatch.question != null
            && this.question.getId() != null && comparedMatch.question.getId() != null) {
            return this.question.getId().equals(comparedMatch.question.getId()) && this.match.equals(comparedMatch.match);
        }
        throw new RuntimeException("Match Can Not Be Compared Without Id or Question Id");
    }

}
