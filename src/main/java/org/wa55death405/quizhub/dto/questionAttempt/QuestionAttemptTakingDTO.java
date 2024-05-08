package org.wa55death405.quizhub.dto.questionAttempt;

import lombok.Data;
import org.wa55death405.quizhub.dto.answerAttempt.AnswerAttemptTakingDTO;
import org.wa55death405.quizhub.dto.choiceAttempt.ChoiceAttemptTakingDTO;
import org.wa55death405.quizhub.dto.optionMatchAttempt.OptionMatchAttemptTakingDTO;
import org.wa55death405.quizhub.dto.orderedOptionAttempt.OrderedOptionAttemptTakingDTO;
import org.wa55death405.quizhub.dto.question.QuestionTakingDTO;
import org.wa55death405.quizhub.dto.quizAttempt.QuizAttemptTakingDTO;
import org.wa55death405.quizhub.entities.*;

import java.util.List;

/*
    * DTO for response of taking a question attempt
 */

@Data
public class QuestionAttemptTakingDTO {
    private Integer id;

    // for TRUE_FALSE,SINGLE_CHOICE,SHORT_ANSWER,NUMERIC,FILL_IN_THE_BLANK,
    private AnswerAttemptTakingDTO answerAttempt;

    // for MULTIPLE_CHOICE, SINGLE_CHOICE
    private List<ChoiceAttemptTakingDTO> choiceAttempts = List.of();

    // for OPTION_ORDERING
    private List<OrderedOptionAttemptTakingDTO> orderedOptionAttempts = List.of();

    // for OPTION_MATCHING
    private List<OptionMatchAttemptTakingDTO> optionMatchAttempts = List.of();

    public QuestionAttemptTakingDTO(QuestionAttempt questionAttempt){
        this.id = questionAttempt.getId();
        if (questionAttempt.getAnswerAttempt() != null){
            this.answerAttempt = new AnswerAttemptTakingDTO(questionAttempt.getAnswerAttempt());
        }
        this.choiceAttempts = questionAttempt.getChoiceAttempts().stream()
                .map(ChoiceAttemptTakingDTO::new)
                .toList();
        this.orderedOptionAttempts = questionAttempt.getOrderedOptionAttempts().stream()
                .map(OrderedOptionAttemptTakingDTO::new)
                .toList();
        this.optionMatchAttempts = questionAttempt.getOptionMatchAttempts().stream()
                .map(OptionMatchAttemptTakingDTO::new)
                .toList();
    }
}
