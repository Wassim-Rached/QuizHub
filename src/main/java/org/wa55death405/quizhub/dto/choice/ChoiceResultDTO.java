package org.wa55death405.quizhub.dto.choice;

import lombok.Data;
import org.wa55death405.quizhub.entities.Choice;

import java.util.UUID;

/*
    * This class represents the data transfer object for the choice result.
    * It contains the id, choice, and isCorrect fields.
 */
@Data
public class ChoiceResultDTO {
    private UUID id;
    private String choice;
    private Boolean isCorrect;

    public ChoiceResultDTO(Choice choice) {
        this.id = choice.getId();
        this.choice = choice.getChoice();
        this.isCorrect = choice.getIsCorrect();
    }
}
