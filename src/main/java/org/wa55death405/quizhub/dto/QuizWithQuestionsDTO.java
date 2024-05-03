package org.wa55death405.quizhub.dto;

import lombok.Data;
import org.wa55death405.quizhub.entities.Question;

import java.util.List;
import java.util.Set;

@Data
public class QuizWithQuestionsDTO {
    private Integer id;
    private String title;
    private List<Question> questions = List.of();
}
