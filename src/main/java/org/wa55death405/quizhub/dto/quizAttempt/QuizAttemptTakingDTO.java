package org.wa55death405.quizhub.dto.quizAttempt;

import lombok.Data;
import org.wa55death405.quizhub.dto.question.QuestionResultDTO;
import org.wa55death405.quizhub.dto.question.QuestionTakingDTO;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptTakingDTO;
import org.wa55death405.quizhub.dto.quiz.QuizGeneralInfoDTO;
import org.wa55death405.quizhub.entities.QuizAttempt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuizAttemptTakingDTO {
    private Integer id;
    private QuizGeneralInfoDTO quiz;
    private List<QuestionTakingDTO> questions;

    public QuizAttemptTakingDTO(QuizAttempt quizAttempt) {
        this.id = quizAttempt.getId();
        this.quiz = new QuizGeneralInfoDTO(quizAttempt.getQuiz());
        this.questions = quizAttempt.getQuiz().getQuestions().stream().map(q ->{
            var qa = quizAttempt.getQuestionAttempts().stream().filter(qa1 -> qa1.getQuestion().getId().equals(q.getId())).findFirst().orElse(null);
            return new QuestionTakingDTO(q, qa);
        }).collect(Collectors.toList());
    }
}
