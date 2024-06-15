package org.wa55death405.quizhub.dto.quiz;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.wa55death405.quizhub.dto.question.QuestionCreationRequestDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.enums.QuizAccessType;
import org.wa55death405.quizhub.exceptions.InputValidationException;
import org.wa55death405.quizhub.interfaces.dto.EntityDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class QuizCreationDTO implements EntityDTO<Quiz,Void> {
    private String title;
    private QuizAccessType quizAccessType;
    private Integer timeLimit;
    private List<QuestionCreationRequestDTO> questions = new ArrayList<>();

    @Override
    public Quiz toEntity(Void aVoid) {
        if (title == null || title.isBlank()) {
            throw new InputValidationException("Quiz 'title' is required");
        }
        if (timeLimit != null && timeLimit < 60) {
            throw new InputValidationException("Quiz 'timeLimit' should be at least 60 seconds");
        }
        if (quizAccessType == null) throw new InputValidationException("Quiz 'quizAccessType' is required");
        Quiz quiz = Quiz.builder()
                .title(title)
                .timeLimit(timeLimit)
                .quizAccessType(quizAccessType)
                .build();
        if (questions == null || questions.size() < Quiz.MIN_QUESTION_COUNT || questions.size() > Quiz.MAX_QUESTION_COUNT) {
            throw new InputValidationException("Quiz should have between " + Quiz.MIN_QUESTION_COUNT + " and " + Quiz.MAX_QUESTION_COUNT + " questions");
        }
        quiz.setQuestions(questions.stream().map(q -> q.toEntity(quiz)).toList());
        return quiz;
    }

    /*
      transform the quiz back to
      the dto that was used to create it
   */
    public QuizCreationDTO(Quiz quiz) {
        this.title = quiz.getTitle();
        this.timeLimit = quiz.getTimeLimit();
        this.quizAccessType = quiz.getQuizAccessType();
        this.questions = quiz.getQuestions().stream().map(QuestionCreationRequestDTO::new).toList();
    }
}
