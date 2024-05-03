package org.wa55death405.quizhub.dto;

import lombok.Data;
import org.wa55death405.quizhub.entities.QuestionAttempt;
import org.wa55death405.quizhub.entities.Quiz;

import java.util.List;

@Data
public class QuizAttemptDTO {
    private Integer id;
    private Float score;
    private QuizGeneralInfoDTO quiz;
    private List<QuestionAttempt> questionAttempts;
}
