package org.wa55death405.quizhub.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Comparator;

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Match {
    @Id
    @GeneratedValue
    private Integer id;
    private String match;

    @ManyToOne
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
        return id.equals(comparedMatch.id);
    }

}
