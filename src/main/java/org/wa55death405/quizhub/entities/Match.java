package org.wa55death405.quizhub.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.Comparator;
import java.util.UUID;

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String match;

    // TODO : match and question should be unique together
    @ManyToOne(optional = false)
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
