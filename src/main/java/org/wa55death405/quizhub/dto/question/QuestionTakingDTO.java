package org.wa55death405.quizhub.dto.question;

import lombok.Data;
import org.wa55death405.quizhub.dto.choice.ChoiceGeneralDTO;
import org.wa55death405.quizhub.dto.match.MatchGeneralDTO;
import org.wa55death405.quizhub.dto.option.OptionGeneralDTO;
import org.wa55death405.quizhub.dto.orderedOption.OrderedOptionGeneralDTO;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptTakingDTO;
import org.wa55death405.quizhub.entities.Question;
import org.wa55death405.quizhub.entities.QuestionAttempt;
import org.wa55death405.quizhub.enums.QuestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
     * This class represents the taking of a question.
     * 'Taking' means the correct answer is not provided.
 */
@Data
public class QuestionTakingDTO {
    private UUID id;
    private String question;
    private Float coefficient = 1f;
    private QuestionType questionType;

    // For MULTIPLE_CHOICE and SINGLE_CHOICE
    private List<ChoiceGeneralDTO> choices;

    // For OPTION_ORDERING
    private List<OrderedOptionGeneralDTO> orderedOptions;

    // For OPTION_MATCHING
    private List<MatchGeneralDTO> matches;
    private List<OptionGeneralDTO> options;

    // For previous attempts
    private QuestionAttemptTakingDTO questionAttempt;

    public QuestionTakingDTO(Question question, QuestionAttempt questionAttempt){
        this.id = question.getId();
        this.question = question.getQuestion();
        this.coefficient = question.getCoefficient();
        this.questionType = question.getQuestionType();
        if (questionAttempt != null){
            this.questionAttempt = new QuestionAttemptTakingDTO(questionAttempt);
        }

        if (!question.getChoices().isEmpty())
            this.choices = question.getChoices().stream()
                .map(ChoiceGeneralDTO::new)
                .toList();

        if (!question.getOrderedOptions().isEmpty())
            this.orderedOptions = question.getOrderedOptions().stream()
                .map(OrderedOptionGeneralDTO::new)
                .toList();

        if (!question.getMatches().isEmpty())
            this.matches = question.getMatches().stream()
                .map(MatchGeneralDTO::new)
                .toList();

        if (!question.getOptions().isEmpty())
            this.options = question.getOptions().stream()
                .map(OptionGeneralDTO::new)
                .toList();
    }
}
