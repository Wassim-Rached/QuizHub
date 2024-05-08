package org.wa55death405.quizhub.dto.choiceAttempt;

import lombok.Data;
import org.wa55death405.quizhub.dto.choice.ChoiceGeneralDTO;
import org.wa55death405.quizhub.entities.ChoiceAttempt;

@Data
public class ChoiceAttemptTakingDTO {
//    private Integer id;
    private ChoiceGeneralDTO choice;

    public ChoiceAttemptTakingDTO(ChoiceAttempt choiceAttempt) {
//        this.id = choiceAttempt.getId();
        this.choice = new ChoiceGeneralDTO(choiceAttempt.getChoice());
    }
}
