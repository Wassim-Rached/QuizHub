package org.wa55death405.quizhub.dto.question;

import lombok.Data;
import org.wa55death405.quizhub.dto.choice.ChoiceGeneralDTO;
import org.wa55death405.quizhub.dto.match.MatchGeneralDTO;
import org.wa55death405.quizhub.dto.option.OptionGeneralDTO;
import org.wa55death405.quizhub.dto.orderedOption.OrderedOptionGeneralDTO;
import org.wa55death405.quizhub.enums.QuestionType;

import java.util.ArrayList;
import java.util.List;

/*
    * DTO for taking a question
    * This DTO is used to take a question
    * It only returns the necessary information for taking a question
    * Without any correct answers
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
}
