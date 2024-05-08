package org.wa55death405.quizhub.dto.answerAttempt;


import lombok.Data;
import org.wa55death405.quizhub.entities.AnswerAttempt;

@Data
public class AnswerAttemptResultDTO {
    private Integer id;
    private Boolean isCorrect;
    private String answer;

    public AnswerAttemptResultDTO(AnswerAttempt answerAttempt) {
        this.id = answerAttempt.getId();
        this.isCorrect = answerAttempt.getIsCorrect();
        this.answer = answerAttempt.getAnswer();
    }
}
