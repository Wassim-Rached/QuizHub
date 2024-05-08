package org.wa55death405.quizhub.dto.question;

import lombok.Data;
import org.wa55death405.quizhub.dto.answer.AnswerResultDTO;
import org.wa55death405.quizhub.dto.choice.ChoiceResultDTO;
import org.wa55death405.quizhub.dto.correctOptionMatch.CorrectOptionMatchResultDTO;
import org.wa55death405.quizhub.dto.match.MatchGeneralDTO;
import org.wa55death405.quizhub.dto.option.OptionGeneralDTO;
import org.wa55death405.quizhub.dto.orderedOption.OrderedOptionResultDTO;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.enums.QuestionType;

import java.util.ArrayList;
import java.util.List;

/*
    * This class represents the result of a question.
    * 'Result' means the correct answer is provided.
 */
@Data
public class QuestionResultDTO {
    private Integer id;
    private String question;
    private Float coefficient = 1f;
    private QuestionType questionType;

    // for TRUE_FALSE,SINGLE_CHOICE,SHORT_ANSWER,NUMERIC,FILL_IN_THE_BLANK,
    private AnswerResultDTO answer;

    // for MULTIPLE_CHOICE, SINGLE_CHOICE
    private List<ChoiceResultDTO> choices = new ArrayList<>();

    // for OPTION_ORDERING
    private List<OrderedOptionResultDTO> orderedOptions = new ArrayList<>();

    // for MATCHING_OPTION
    private List<MatchGeneralDTO> matches = new ArrayList<>();
    private List<OptionGeneralDTO> options = new ArrayList<>();
    private List<CorrectOptionMatchResultDTO> correctOptionMatches = new ArrayList<>();

    public QuestionResultDTO(Question question) {
        this.id = question.getId();
        this.question = question.getQuestion();
        this.coefficient = question.getCoefficient();
        this.questionType = question.getQuestionType();
        if (question.getAnswer() != null){
            this.answer = new AnswerResultDTO(question.getAnswer());
        }
        if (question.getChoices() != null) {
            for (Choice choice : question.getChoices()) {
                choices.add(new ChoiceResultDTO(choice));
            }
        }
        if (question.getOrderedOptions() != null) {
            for (OrderedOption orderedOption : question.getOrderedOptions()) {
                orderedOptions.add(new OrderedOptionResultDTO(orderedOption));
            }
        }
        if (question.getMatches() != null) {
            for (Match match : question.getMatches()) {
                matches.add(new MatchGeneralDTO(match));
            }
        }
        if (question.getOptions() != null) {
            for (Option option : question.getOptions()) {
                options.add(new OptionGeneralDTO(option));
            }
        }
        if (question.getCorrectOptionMatches() != null) {
            for (CorrectOptionMatch correctOptionMatch : question.getCorrectOptionMatches()) {
                correctOptionMatches.add(new CorrectOptionMatchResultDTO(correctOptionMatch));
            }
        }
    }
}
