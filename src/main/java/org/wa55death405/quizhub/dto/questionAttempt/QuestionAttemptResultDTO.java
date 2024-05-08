package org.wa55death405.quizhub.dto.questionAttempt;

import lombok.Data;
import org.wa55death405.quizhub.dto.answerAttempt.AnswerAttemptResultDTO;
import org.wa55death405.quizhub.dto.choiceAttempt.ChoiceAttemptResultDTO;
import org.wa55death405.quizhub.dto.optionMatchAttempt.OptionMatchAttemptResult;
import org.wa55death405.quizhub.dto.orderedOptionAttempt.OrderedOptionAttemptResult;
import org.wa55death405.quizhub.dto.question.QuestionResultDTO;
import org.wa55death405.quizhub.entities.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionAttemptResultDTO {
    private Integer id;
    private Float correctnessPercentage;
    private QuestionResultDTO question;

    // for TRUE_FALSE,SINGLE_CHOICE,SHORT_ANSWER,NUMERIC,FILL_IN_THE_BLANK,
    private AnswerAttemptResultDTO answerAttempt;

    // for MULTIPLE_CHOICE, SINGLE_CHOICE
    private List<ChoiceAttemptResultDTO> choiceAttempts = new ArrayList<>();

    // for OPTION_ORDERING
    private List<OrderedOptionAttemptResult> orderedOptionAttempts = new ArrayList<>();

    // for OPTION_MATCHING
    private List<OptionMatchAttemptResult> optionMatchAttempts = new ArrayList<>();

    public QuestionAttemptResultDTO(QuestionAttempt questionAttempt){
        this.id = questionAttempt.getId();
        this.correctnessPercentage = questionAttempt.getCorrectnessPercentage();
        this.question = new QuestionResultDTO(questionAttempt.getQuestion());
        if (questionAttempt.getAnswerAttempt() != null){
            this.answerAttempt = new AnswerAttemptResultDTO(questionAttempt.getAnswerAttempt());
        }
        if(questionAttempt.getChoiceAttempts() != null){
            for(ChoiceAttempt choiceAttempt : questionAttempt.getChoiceAttempts()){
                choiceAttempts.add(new ChoiceAttemptResultDTO(choiceAttempt));
            }
        }
        if(questionAttempt.getOrderedOptionAttempts() != null){
            for(OrderedOptionAttempt orderedOptionAttempt : questionAttempt.getOrderedOptionAttempts()){
                orderedOptionAttempts.add(new OrderedOptionAttemptResult(orderedOptionAttempt));
            }
        }
        if(questionAttempt.getOptionMatchAttempts() != null){
            for(OptionMatchAttempt optionMatchAttempt : questionAttempt.getOptionMatchAttempts()){
                optionMatchAttempts.add(new OptionMatchAttemptResult(optionMatchAttempt));
            }
        }
    }
}
