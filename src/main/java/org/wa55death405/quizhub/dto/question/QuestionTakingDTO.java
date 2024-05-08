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

/*
     * This class represents the taking of a question.
     * 'Taking' means the correct answer is not provided.
 */
@Data
public class QuestionTakingDTO {
    private Integer id;
    private String question;
    private Float coefficient = 1f;
    private QuestionType questionType;

    // For MULTIPLE_CHOICE and SINGLE_CHOICE
    private List<ChoiceGeneralDTO> choices = new ArrayList<>();

    // For OPTION_ORDERING
    private List<OrderedOptionGeneralDTO> orderedOptions = new ArrayList<>();

    // For OPTION_MATCHING
    private List<MatchGeneralDTO> matches = new ArrayList<>();
    private List<OptionGeneralDTO> options = new ArrayList<>();

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
        this.choices = question.getChoices().stream()
                .map(ChoiceGeneralDTO::new)
                .toList();
        this.orderedOptions = question.getOrderedOptions().stream()
                .map(OrderedOptionGeneralDTO::new)
                .toList();
        this.matches = question.getMatches().stream()
                .map(MatchGeneralDTO::new)
                .toList();
        this.options = question.getOptions().stream()
                .map(OptionGeneralDTO::new)
                .toList();
    }
}
