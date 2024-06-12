package org.wa55death405.quizhub.dto.quiz;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.wa55death405.quizhub.dto.question.QuestionCreationRequestDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.exceptions.InputValidationException;
import org.wa55death405.quizhub.interfaces.dto.EntityDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class QuizCreationDTO implements EntityDTO<Quiz,Void> {
    private String title;
    private List<QuestionCreationRequestDTO> questions = new ArrayList<>();

    @Override
    public Quiz toEntity(Void aVoid) {
        if (title == null || title.isBlank()) {
            throw new InputValidationException("Quiz 'title' is required");
        }
        Quiz quiz = Quiz.builder()
                .title(title)
                .build();
        if (questions == null || questions.size() < Quiz.MIN_QUESTION_COUNT || questions.size() > Quiz.MAX_QUESTION_COUNT) {
            throw new InputValidationException("Quiz should have between " + Quiz.MIN_QUESTION_COUNT + " and " + Quiz.MAX_QUESTION_COUNT + " questions");
        }
        quiz.setQuestions(questions.stream().map(q -> q.toEntity(quiz)).toList());
        return quiz;
    }

    public QuizCreationDTO(Quiz quiz) {
        this.title = quiz.getTitle();
        this.questions = quiz.getQuestions().stream().map(QuestionCreationRequestDTO::new).toList();
    }
}
