package org.wa55death405.quizhub.dto.quiz;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.enums.QuizAccessType;

import java.util.UUID;

@Data
@NoArgsConstructor
public class QuizGeneralInfoDTO {
    private UUID id;
    private String title;
    private Integer timeLimit;
    private QuizAccessType quizAccessType;

    public QuizGeneralInfoDTO(Quiz quiz) {
        this.id = quiz.getId();
        this.title = quiz.getTitle();
        this.timeLimit = quiz.getTimeLimit();
        this.quizAccessType = quiz.getQuizAccessType();
    }
}
