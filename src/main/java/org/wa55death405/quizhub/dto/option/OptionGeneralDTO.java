package org.wa55death405.quizhub.dto.option;

import lombok.Data;
import org.wa55death405.quizhub.entities.Option;

import java.util.UUID;

@Data
public class OptionGeneralDTO {
    private UUID id;
    private String option;

    public OptionGeneralDTO(Option option) {
        this.id = option.getId();
        this.option = option.getOption();
    }
}
