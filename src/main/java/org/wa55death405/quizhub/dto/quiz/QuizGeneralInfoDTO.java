package org.wa55death405.quizhub.dto.quiz;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.wa55death405.quizhub.entities.Quiz;

@Data
@NoArgsConstructor
public class QuizGeneralInfoDTO {
    private Integer id;
    private String title;

    public QuizGeneralInfoDTO(Quiz quiz) {
        this.id = quiz.getId();
        this.title = quiz.getTitle();
    }
}
