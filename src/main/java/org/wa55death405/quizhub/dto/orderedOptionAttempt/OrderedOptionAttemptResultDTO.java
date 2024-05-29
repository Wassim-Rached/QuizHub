package org.wa55death405.quizhub.dto.orderedOptionAttempt;

import lombok.Data;
import org.wa55death405.quizhub.entities.OrderedOptionAttempt;

import java.util.UUID;

@Data
public class OrderedOptionAttemptResultDTO {
    private UUID id;
    private Integer position;
    private Boolean isCorrect;

    // TODO : i think something is worng here where is the option id ? and who's this id
    public OrderedOptionAttemptResultDTO(OrderedOptionAttempt orderedOptionAttempt) {
        this.id = orderedOptionAttempt.getId();
        this.position = orderedOptionAttempt.getPosition();
        this.isCorrect = orderedOptionAttempt.getIsCorrect();
    }
}
