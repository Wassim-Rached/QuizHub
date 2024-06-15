package org.wa55death405.quizhub.dto.question;

import lombok.Data;
import org.wa55death405.quizhub.dto.answer.AnswerResultDTO;
import org.wa55death405.quizhub.dto.choice.ChoiceResultDTO;
import org.wa55death405.quizhub.dto.correctOptionMatch.CorrectOptionMatchResultDTO;
import org.wa55death405.quizhub.dto.match.MatchGeneralDTO;
import org.wa55death405.quizhub.dto.option.OptionGeneralDTO;
import org.wa55death405.quizhub.dto.orderedOption.OrderedOptionResultDTO;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptResultDTO;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.enums.QuestionType;
import org.wa55death405.quizhub.exceptions.InputValidationException;
import org.wa55death405.quizhub.exceptions.IrregularBehaviorException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
    * This class represents the result of a question.
    * 'Result' means the correct answer is provided.
 */
@Data
public class QuestionResultDTO {
    private UUID id;
    private String question;
    private Float coefficient = 1f;
    private QuestionType questionType;
    private String[] questionNotes;

    // for FILL_IN_THE_BLANK
    private String paragraphToBeFilled;

    // for TRUE_FALSE,SINGLE_CHOICE,SHORT_ANSWER,NUMERIC,FILL_IN_THE_BLANK,
    private AnswerResultDTO answer;

    // for MULTIPLE_CHOICE, SINGLE_CHOICE
    private List<ChoiceResultDTO> choices;

    // for OPTION_ORDERING
    private List<OrderedOptionResultDTO> orderedOptions;

    // for MATCHING_OPTION
    private List<MatchGeneralDTO> matches;
    private List<OptionGeneralDTO> options;
    private List<CorrectOptionMatchResultDTO> correctOptionMatches;

    // to show the play's attempt
    private QuestionAttemptResultDTO questionAttempt;

    public QuestionResultDTO(Question question,QuestionAttempt questionAttempt) {
        this.id = question.getId();
        this.question = question.getQuestion();
        this.coefficient = question.getCoefficient();
        this.questionType = question.getQuestionType();

        if (question.getQuestionNotes() != null && !question.getQuestionNotes().isEmpty()){
            this.questionNotes = question.getQuestionNotes().stream()
                .map(QuestionNote::getNote)
                .toArray(String[]::new);
        }

        if (questionAttempt != null){
            this.questionAttempt = new QuestionAttemptResultDTO(questionAttempt);
        }

        switch (this.questionType){
            case TRUE_FALSE,FILL_IN_THE_BLANK,NUMERIC,SHORT_ANSWER->{
                if (question.getAnswers() == null || question.getAnswers().isEmpty())
                    throw new IrregularBehaviorException("Answer is required for question of type " + this.questionType);
                this.answer = new AnswerResultDTO(question.getAnswers().get(0));

                if (this.questionType == QuestionType.FILL_IN_THE_BLANK){
                    this.paragraphToBeFilled = question.getParagraphToBeFilled();
                }
            }
            case MULTIPLE_CHOICE,SINGLE_CHOICE -> {
                choices = new ArrayList<>();
                for (Choice choice : question.getChoices()) {
                    choices.add(new ChoiceResultDTO(choice));
                }
            }
            case OPTION_MATCHING -> {
                matches = new ArrayList<>();
                options = new ArrayList<>();
                correctOptionMatches = new ArrayList<>();

                for (Match match : question.getMatches()) {
                    matches.add(new MatchGeneralDTO(match));
                }

                for (Option option : question.getOptions()) {
                    options.add(new OptionGeneralDTO(option));
                }

                for (CorrectOptionMatch correctOptionMatch : question.getCorrectOptionMatches()) {
                    correctOptionMatches.add(new CorrectOptionMatchResultDTO(correctOptionMatch));
                }
            }
            case OPTION_ORDERING -> {
                orderedOptions = new ArrayList<>();
                for (OrderedOption orderedOption : question.getOrderedOptions()) {
                    orderedOptions.add(new OrderedOptionResultDTO(orderedOption));
                }
            }
            default -> {throw new IllegalArgumentException("Invalid question type");}
        }
    }
}
