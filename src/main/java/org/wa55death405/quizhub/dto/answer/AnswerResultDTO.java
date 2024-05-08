package org.wa55death405.quizhub.dto.answer;

import lombok.Data;
import org.wa55death405.quizhub.entities.Answer;

@Data
public class AnswerResultDTO {
//    private Integer id;
    private String answer;

    public AnswerResultDTO(Answer answer) {
//        this.id = answer.getId();
        this.answer = answer.getAnswer();
    }
}
