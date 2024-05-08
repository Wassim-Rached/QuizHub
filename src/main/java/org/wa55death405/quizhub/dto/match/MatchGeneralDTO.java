package org.wa55death405.quizhub.dto.match;

import lombok.Data;
import org.wa55death405.quizhub.entities.Match;

@Data
public class MatchGeneralDTO {
    private Integer id;
    private String match;

    public MatchGeneralDTO(Match match) {
        this.id = match.getId();
        this.match = match.getMatch();
    }
}
