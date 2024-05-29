package org.wa55death405.quizhub.dto.quiz;

import lombok.Data;
import org.wa55death405.quizhub.dto.question.QuestionTakingDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptTakingDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO: should be removed no one is using it
@Data
public class QuizTakingDTO {
    private UUID id;
    private String title;
    private List<QuestionTakingDTO> questions = new ArrayList<>();
    private QuizAttemptTakingDTO quizAttempt;
}
