package org.wa55death405.quizhub.dto.choiceAttempt;

import lombok.Data;
import org.wa55death405.quizhub.dto.choice.ChoiceGeneralDTO;
import org.wa55death405.quizhub.entities.ChoiceAttempt;

@Data
public class ChoiceAttemptResultDTO {
//    private Integer id;
    private ChoiceGeneralDTO choice;
    private Boolean isCorrect;

    public ChoiceAttemptResultDTO(ChoiceAttempt choiceAttempt) {
//        this.id = choiceAttempt.getId();
        this.choice = new ChoiceGeneralDTO(choiceAttempt.getChoice());
        this.isCorrect = choiceAttempt.getIsCorrect();
    }
}
