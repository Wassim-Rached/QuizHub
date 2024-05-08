package org.wa55death405.quizhub.dto.optionMatchAttempt;

import lombok.Data;
import org.wa55death405.quizhub.dto.match.MatchGeneralDTO;
import org.wa55death405.quizhub.dto.option.OptionGeneralDTO;
import org.wa55death405.quizhub.entities.OptionMatchAttempt;

@Data
public class OptionMatchAttemptTakingDTO {
    private Integer id;
    private MatchGeneralDTO match;
    private OptionGeneralDTO option;

    public OptionMatchAttemptTakingDTO(OptionMatchAttempt optionMatchAttempt) {
        this.id = optionMatchAttempt.getId();
        this.match = new MatchGeneralDTO(optionMatchAttempt.getMatch());
        this.option = new OptionGeneralDTO(optionMatchAttempt.getOption());
    }
}
