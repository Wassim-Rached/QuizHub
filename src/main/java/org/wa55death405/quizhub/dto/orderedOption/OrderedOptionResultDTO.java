package org.wa55death405.quizhub.dto.orderedOption;

import lombok.Data;
import org.wa55death405.quizhub.entities.OrderedOption;

@Data
public class OrderedOptionResultDTO {
    private Integer id;
    private String option;
    private Integer correctPosition;

    public OrderedOptionResultDTO(OrderedOption orderedOption) {
        this.id = orderedOption.getId();
        this.option = orderedOption.getOption();
        this.correctPosition = orderedOption.getCorrectPosition();
    }
}
