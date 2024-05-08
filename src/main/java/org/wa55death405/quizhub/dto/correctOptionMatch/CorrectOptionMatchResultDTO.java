package org.wa55death405.quizhub.dto.correctOptionMatch;

import lombok.Data;
import org.wa55death405.quizhub.dto.match.MatchGeneralDTO;
import org.wa55death405.quizhub.dto.option.OptionGeneralDTO;
import org.wa55death405.quizhub.entities.CorrectOptionMatch;

@Data
public class CorrectOptionMatchResultDTO {
    private MatchGeneralDTO match;
    private OptionGeneralDTO option;

    public CorrectOptionMatchResultDTO(CorrectOptionMatch correctOptionMatch) {
        this.match = new MatchGeneralDTO(correctOptionMatch.getMatch());
        this.option = new OptionGeneralDTO(correctOptionMatch.getOption());
    }
}
