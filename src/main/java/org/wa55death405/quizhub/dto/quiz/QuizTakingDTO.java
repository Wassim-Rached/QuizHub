package org.wa55death405.quizhub.dto.quiz;

import lombok.Data;
import org.wa55death405.quizhub.dto.question.QuestionTakingDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptTakingDTO;
import org.wa55death405.quizhub.entities.Question;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuizTakingDTO {
    private Integer id;
    private String title;
    private List<QuestionTakingDTO> questions = new ArrayList<>();
    private QuizAttemptTakingDTO quizAttempt;
}
