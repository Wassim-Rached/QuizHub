package org.wa55death405.quizhub.dto.questionAttempt;

import lombok.Data;
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
//    private Float correctnessPercentage;
    private QuestionTakingDTO question;
    private QuizAttemptTakingDTO quizAttempt;

    // for TRUE_FALSE,SINGLE_CHOICE,SHORT_ANSWER,NUMERIC,FILL_IN_THE_BLANK,
    private AnswerAttempt answerAttempt;

    // for MULTIPLE_CHOICE, SINGLE_CHOICE
    private List<ChoiceAttempt> choiceAttempts = List.of();

    // for OPTION_ORDERING
    private List<OrderedOptionAttempt> orderedOptionAttempts = List.of();

    // for OPTION_MATCHING
    private List<OptionMatchAttempt> optionMatchAttempts = List.of();

}
