package org.wa55death405.quizhub.dto.orderedOptionAttempt;

import lombok.Data;
import org.wa55death405.quizhub.dto.orderedOption.OrderedOptionGeneralDTO;
import org.wa55death405.quizhub.entities.OrderedOptionAttempt;

import java.util.UUID;

@Data
public class OrderedOptionAttemptResultDTO {
    private UUID id;
    private Integer position;
    private OrderedOptionGeneralDTO orderedOption;
    private Boolean isCorrect;

//     TODO: the 'id' field is might be unnecessary
    public OrderedOptionAttemptResultDTO(OrderedOptionAttempt orderedOptionAttempt) {
        this.id = orderedOptionAttempt.getId();
        this.position = orderedOptionAttempt.getPosition();
        this.isCorrect = orderedOptionAttempt.getIsCorrect();
        this.orderedOption = new OrderedOptionGeneralDTO(orderedOptionAttempt.getOrderedOption());
    }
}
