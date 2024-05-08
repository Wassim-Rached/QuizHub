package org.wa55death405.quizhub.dto.orderedOptionAttempt;

import lombok.Data;
import org.wa55death405.quizhub.entities.OrderedOptionAttempt;

@Data
public class OrderedOptionAttemptTakingDTO {
    private Integer id;
    private Integer position;

    public OrderedOptionAttemptTakingDTO(OrderedOptionAttempt orderedOptionAttempt) {
        this.id = orderedOptionAttempt.getId();
        this.position = orderedOptionAttempt.getPosition();
    }
}
