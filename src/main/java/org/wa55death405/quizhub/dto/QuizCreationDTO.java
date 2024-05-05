package org.wa55death405.quizhub.dto;

import lombok.Data;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.interfaces.EntityDTO;

import java.util.List;

@Data
public class QuizCreationDTO implements EntityDTO<Quiz,Void> {
    private String title;
    private List<QuestionCreationDTO> questions;

    @Override
    public Quiz toEntity(Void aVoid) {
        Quiz quiz = Quiz.builder()
                .title(title)
                .build();
        quiz.setQuestions(questions.stream().map(q -> q.toEntity(quiz)).toList());
        return quiz;
    }
}
