package org.wa55death405.quizhub.dto;

import lombok.Data;
import org.wa55death405.quizhub.entities.Choice;
import org.wa55death405.quizhub.entities.Match;
import org.wa55death405.quizhub.entities.Option;
import org.wa55death405.quizhub.entities.OrderedOption;
import org.wa55death405.quizhub.enums.QuestionType;

import java.util.List;

@Data
public class QuestionTakingDTO {
    private Integer id;
    private String question;
    private Float coefficient = 1f;
    private QuestionType questionType;

    // For MULTIPLE_CHOICE and SINGLE_CHOICE
    private List<Choice> choices = List.of();

    // For OPTION_ORDERING
    private List<OrderedOption> orderedOptions = List.of();

    // For OPTION_MATCHING
    private List<Match> matches = List.of();
    private List<Option> options = List.of();
}
