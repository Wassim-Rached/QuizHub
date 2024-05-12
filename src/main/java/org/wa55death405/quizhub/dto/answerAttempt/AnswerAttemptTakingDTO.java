package org.wa55death405.quizhub.dto.answerAttempt;

import lombok.Data;
import org.wa55death405.quizhub.entities.AnswerAttempt;

@Data
public class AnswerAttemptTakingDTO {
//    private Integer id;
    private String answer;

    public AnswerAttemptTakingDTO(AnswerAttempt answerAttempt) {
//        this.id = answerAttempt.getId();
        this.answer = answerAttempt.getAnswer();
    }
}
