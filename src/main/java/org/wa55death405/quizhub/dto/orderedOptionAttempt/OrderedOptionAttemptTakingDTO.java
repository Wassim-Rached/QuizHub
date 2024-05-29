package org.wa55death405.quizhub.dto.orderedOptionAttempt;

import lombok.Data;
import org.wa55death405.quizhub.entities.OrderedOptionAttempt;

import java.util.UUID;

@Data
public class OrderedOptionAttemptTakingDTO {
    private UUID id;
    private Integer position;

    // TODO : i think something is worng here where is the option id ? and who's this id
    public OrderedOptionAttemptTakingDTO(OrderedOptionAttempt orderedOptionAttempt) {
        this.id = orderedOptionAttempt.getId();
        this.position = orderedOptionAttempt.getPosition();
    }
}
