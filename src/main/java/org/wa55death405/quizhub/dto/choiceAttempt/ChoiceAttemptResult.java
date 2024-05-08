package org.wa55death405.quizhub.dto.choiceAttempt;

import lombok.Data;
import org.wa55death405.quizhub.dto.choice.ChoiceGeneralDTO;
import org.wa55death405.quizhub.entities.ChoiceAttempt;

@Data
public class ChoiceAttemptResult {
    private ChoiceGeneralDTO choice;
    private Boolean isCorrect;

    public ChoiceAttemptResult(ChoiceAttempt choiceAttempt) {
        this.choice = new ChoiceGeneralDTO(choiceAttempt.getChoice());
        this.isCorrect = choiceAttempt.getIsCorrect();
    }
}
