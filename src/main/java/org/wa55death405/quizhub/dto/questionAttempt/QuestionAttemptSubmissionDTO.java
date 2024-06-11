package org.wa55death405.quizhub.dto.questionAttempt;

import lombok.Data;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.exceptions.InputValidationException;
import org.wa55death405.quizhub.interfaces.dto.EntityDTO;

import java.util.*;

/*
    * DTO class for 'QuestionAttempt' entity
    * This class is used to convert the incoming JSON request to 'QuestionAttempt' entity
 */
@Data
public class QuestionAttemptSubmissionDTO implements EntityDTO<QuestionAttempt,UUID> {
//    TODO rename question to questionId
    private UUID question;

    // for TRUE_FALSE,SINGLE_CHOICE,SHORT_ANSWER,NUMERIC,FILL_IN_THE_BLANK
    private String answerAttempt;

    // for MULTIPLE_CHOICE, SINGLE_CHOICE
    private List<UUID> choiceAttempts;

    // for OPTION_ORDERING
    private HashMap<Integer,UUID> orderedOptionAttempts;

    // for OPTION_MATCHING: <optionId, List<matchId>>
    private HashMap<UUID,Set<UUID>> optionMatchAttempts;

    @Override
    public QuestionAttempt toEntity(UUID quizAttempt) {
        if (question == null) throw new InputValidationException("Question id is required");

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
        if(answerAttempt != null){
            questionAttempt.setAnswerAttempt(
                    AnswerAttempt.builder()
                            .answer(answerAttempt)
                            .questionAttempt(questionAttempt)
                            .build());
        }

        // convert choiceAttempt ids to choiceAttempt objects
        if (choiceAttempts != null)
            choiceAttempts.forEach(choiceAttempt -> choiceAttemptsObjs.add(ChoiceAttempt.builder().choice(Choice.builder().id(choiceAttempt).build()).questionAttempt(questionAttempt).build()));

        // convert orderedOptionAttempts to orderedOptionAttempt objects
        if (orderedOptionAttempts != null)
            orderedOptionAttempts.forEach((key, value) -> orderedOptionAttemptsObjs.add(OrderedOptionAttempt.builder().orderedOption(OrderedOption.builder().id(value).build()).position(key).questionAttempt(questionAttempt).build()));

        // convert optionMatchAttempts to optionMatchAttempt objects
        if (optionMatchAttempts != null)
            optionMatchAttempts.forEach((key, value) -> {
                Option option = Option.builder().id(key).build();
                value.forEach(matchId -> optionMatchAttemptsObjs.add(OptionMatchAttempt.builder().option(option).match(Match.builder().id(matchId).build()).questionAttempt(questionAttempt).build()));
            });

        return questionAttempt;
    }
}
