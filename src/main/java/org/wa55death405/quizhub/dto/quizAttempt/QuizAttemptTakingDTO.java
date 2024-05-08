package org.wa55death405.quizhub.dto.quizAttempt;

import lombok.Data;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptTakingDTO;
import org.wa55death405.quizhub.dto.quiz.QuizGeneralInfoDTO;
import org.wa55death405.quizhub.entities.QuizAttempt;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuizAttemptTakingDTO {
    private Integer id;
    private QuizGeneralInfoDTO quiz;
    private List<QuestionAttemptTakingDTO> questionAttempts = new ArrayList<>();

    public QuizAttemptTakingDTO(QuizAttempt quizAttempt) {
        this.id = quizAttempt.getId();
        this.quiz = new QuizGeneralInfoDTO(quizAttempt.getQuiz());
        this.questionAttempts = quizAttempt.getQuestionAttempts().stream()
                .map(QuestionAttemptTakingDTO::new)
                .toList();
    }
}
