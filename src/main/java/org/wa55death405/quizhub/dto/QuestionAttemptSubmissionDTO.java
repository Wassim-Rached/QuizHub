package org.wa55death405.quizhub.dto;

import lombok.Data;
import org.wa55death405.quizhub.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class QuestionAttemptSubmissionDTO {
    private Integer question;

    // for TRUE_FALSE,SINGLE_CHOICE,SHORT_ANSWER,NUMERIC,FILL_IN_THE_BLANK
    private String answerAttempt;

    // for MULTIPLE_CHOICE, SINGLE_CHOICE
    private List<Integer> choiceAttempts = new ArrayList<>();

    // for OPTION_ORDERING
    private HashMap<Integer,Integer> orderedOptionAttempts = new HashMap<>();

    // for OPTION_MATCHING
    private HashMap<Integer,List<Integer>> optionMatchAttempts = new HashMap<>();

    public QuestionAttempt toQuestionAttempt(Integer quizAttempt) {
        List<ChoiceAttempt> choiceAttemptsObjs = new ArrayList<>();
        List<OrderedOptionAttempt> orderedOptionAttemptsObjs = new ArrayList<>();
        List<OptionMatchAttempt> optionMatchAttemptsObjs = new ArrayList<>();

        // convert questionAttemptSubmissionDTO to QuestionAttempt object
        var questionAttempt =  QuestionAttempt.builder()
                .question(Question.builder().id(question).build())
                .quizAttempt(QuizAttempt.builder().id(quizAttempt).build())
                .choiceAttempts(choiceAttemptsObjs)
                .orderedOptionAttempts(orderedOptionAttemptsObjs)
                .optionMatchAttempts(optionMatchAttemptsObjs)
                .build();


        // convert answerAttempt to AnswerAttempt object
        if(answerAttempt != null && !answerAttempt.isEmpty()){
            questionAttempt.setAnswerAttempt(
                    AnswerAttempt.builder()
                            .answer(answerAttempt)
                            .questionAttempt(questionAttempt)
                            .build());
        }

        // convert choiceAttempt ids to choiceAttempt objects
        choiceAttempts.forEach(choiceAttempt -> choiceAttemptsObjs.add(ChoiceAttempt.builder().choice(Choice.builder().id(choiceAttempt).build()).questionAttempt(questionAttempt).build()));

        // convert orderedOptionAttempts to orderedOptionAttempt objects
        orderedOptionAttempts.forEach((key, value) -> orderedOptionAttemptsObjs.add(OrderedOptionAttempt.builder().orderedOption(OrderedOption.builder().id(value).build()).position(key).questionAttempt(questionAttempt).build()));

        // convert optionMatchAttempts to optionMatchAttempt objects
        optionMatchAttempts.forEach((key, value) -> {
            Option option = Option.builder().id(key).build();
            value.forEach(matchId -> optionMatchAttemptsObjs.add(OptionMatchAttempt.builder().option(option).match(Match.builder().id(matchId).build()).questionAttempt(questionAttempt).build()));
        });

        return questionAttempt;
    }

}
