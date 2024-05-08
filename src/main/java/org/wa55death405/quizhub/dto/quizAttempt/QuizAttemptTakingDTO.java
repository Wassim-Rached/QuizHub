package org.wa55death405.quizhub.dto.quizAttempt;

import lombok.Data;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptTakingDTO;
import org.wa55death405.quizhub.dto.quiz.QuizGeneralInfoDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuizAttemptTakingDTO {
    private Integer id;
    private QuizGeneralInfoDTO quiz;
    private List<QuestionAttemptTakingDTO> questionAttempts = new ArrayList<>();
}
