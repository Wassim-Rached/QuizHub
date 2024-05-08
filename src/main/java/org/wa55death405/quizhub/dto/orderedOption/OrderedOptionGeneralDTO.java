package org.wa55death405.quizhub.dto.orderedOption;

import lombok.Data;
import org.wa55death405.quizhub.entities.OrderedOption;

@Data
public class OrderedOptionGeneralDTO {
    private Integer id;
    private String option;

    public OrderedOptionGeneralDTO(OrderedOption orderedOption) {
        this.id = orderedOption.getId();
        this.option = orderedOption.getOption();
    }
}