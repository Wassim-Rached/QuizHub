package org.wa55death405.quizhub.dto.choice;

import lombok.Data;
import org.wa55death405.quizhub.entities.Choice;

import java.util.UUID;

@Data
public class ChoiceGeneralDTO {
    private UUID id;
    private String choice;

    public ChoiceGeneralDTO(Choice choice) {
        this.id = choice.getId();
        this.choice = choice.getChoice();
    }
}
