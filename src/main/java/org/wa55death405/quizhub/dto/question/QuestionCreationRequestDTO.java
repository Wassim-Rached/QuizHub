package org.wa55death405.quizhub.dto.question;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.enums.QuestionType;
import org.wa55death405.quizhub.exceptions.InputValidationException;
import org.wa55death405.quizhub.interfaces.dto.EntityDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
public class QuestionCreationRequestDTO implements EntityDTO<Question,Quiz> {
    private String question;
    private QuestionType questionType;
    private Float coefficient;
    private List<String> answers;
    private String paragraphToBeFilled;
    private HashMap<String,Boolean> choices = new HashMap<>();
    private HashMap<Integer,String> orderedOptions = new HashMap<>();
    // HashMap<match, [option1, option2, ...]>
    private HashMap<String,List<String>> optionMatches = new HashMap<>();

    /*
        transform the question back to
        the dto that was used to create it
     */
    public QuestionCreationRequestDTO(Question question) {
        this.question = question.getQuestion();
        this.questionType = question.getQuestionType();
        this.coefficient = question.getCoefficient();

        switch (questionType) {
            case TRUE_FALSE,SHORT_ANSWER,NUMERIC,FILL_IN_THE_BLANK:
                this.answers = question.getAnswers().stream().map(Answer::getAnswer).toList();
                break;

            case MULTIPLE_CHOICE:
            case SINGLE_CHOICE:
                question.getChoices().forEach(choice -> choices.put(choice.getChoice(), choice.getIsCorrect()));
                break;

            case OPTION_ORDERING:
                question.getOrderedOptions().forEach(orderedOption -> orderedOptions.put(orderedOption.getCorrectPosition(), orderedOption.getOption()));
                break;

            case OPTION_MATCHING:
                // TODO: this might not work as expected
                /*
                    the double call on:
                        * the getMatch().getMatch() method
                        * and the getOption().getOption() method
                    is because the first one is for the entity instance (Match, Option)
                    and the second one is for the value of the entity (String)
                 */
                for (Match match : question.getMatches()){
                    optionMatches.put(match.getMatch(), new ArrayList<>());
                    for (CorrectOptionMatch correctOptionMatch : question.getCorrectOptionMatches()){
                        if (correctOptionMatch.getMatch().getMatch().equals(match.getMatch())){
                            optionMatches.get(match.getMatch()).add(correctOptionMatch.getOption().getOption());
                        }
                    }
                }
                break;
        }
    }

    /*
        transform the dto to the question entity
        the quiz is used as a reference to the question
     */
    @Override
    public Question toEntity(Quiz quiz) {
        if (coefficient <= 0) {
            throw new InputValidationException("Coefficient must be greater than 0 for question '" + this.question + "'");
        }
        Question question = new Question();
        question.setQuestion(this.question);
        question.setQuestionType(this.questionType);
        question.setCoefficient(coefficient);
        question.setQuiz(quiz);

        switch (questionType) {
            case TRUE_FALSE,SHORT_ANSWER,NUMERIC,FILL_IN_THE_BLANK:
                if (this.answers == null || this.answers.isEmpty()) {
                    throw new InputValidationException("Answer is required for question '" + this.question + "' of type " + this.questionType);
                }
                question.setAnswers(
                        this.answers.stream()
                                .map(answer -> Answer.builder().answer(answer).question(question).build())
                                .toList()
                );
                if (this.questionType == QuestionType.FILL_IN_THE_BLANK) {
                    if (this.paragraphToBeFilled == null || this.paragraphToBeFilled.isBlank()) {
                        throw new InputValidationException("Paragraph to be filled is required for question '" + this.question + "' of type " + this.questionType);
                    }
                    question.setParagraphToBeFilled(this.paragraphToBeFilled);
                }
                break;

            case MULTIPLE_CHOICE:
            case SINGLE_CHOICE:
                if (this.choices == null || this.choices.isEmpty()) {
                    throw new InputValidationException("Choices are required for question '" + this.question + "' of type " + this.questionType);
                }
                if (this.questionType == QuestionType.SINGLE_CHOICE) {
                    if (this.choices.values().stream().filter(Boolean::booleanValue).count() != 1) {
                        throw new InputValidationException("Exactly one choice must be correct for question '" + this.question + "' of type " + this.questionType);
                    }
                }
                var choicesList = this.choices.entrySet().stream()
                        .map(entry -> Choice.builder().choice(entry.getKey()).isCorrect(entry.getValue()).question(question).build()).toList();
                question.setChoices(choicesList);
                break;

            case OPTION_ORDERING:
                if (this.orderedOptions == null || this.orderedOptions.isEmpty()) {
                    throw new InputValidationException("Ordered options are required for question '" + this.question + "' of type " + this.questionType);
                }
                var orderedOptions = new ArrayList<OrderedOption>();
                for (var entry : this.orderedOptions.entrySet()) {
                    orderedOptions.add(OrderedOption.builder()
                            .correctPosition(entry.getKey())
                            .option(entry.getValue())
                            .question(question)
                            .build());
                }
                question.setOrderedOptions(orderedOptions);
                break;

            case OPTION_MATCHING:
                if (this.optionMatches == null || this.optionMatches.isEmpty()) {
                    throw new InputValidationException("Option matches are required for question '" + this.question + "' of type " + this.questionType);
                }
                var correctOptionMatches = new ArrayList<CorrectOptionMatch>();
                var standaloneMatches = new ArrayList<Match>();
                var optionsHashMap = new HashMap<String,Option>();
                for (var entry : this.optionMatches.entrySet()) {
                    List<String> optionValues = entry.getValue();
                    String matchValue = entry.getKey();
                    var match = Match.builder().match(matchValue).question(question).build();

                    if (optionValues == null || optionValues.isEmpty()) {
                        standaloneMatches.add(match);
                        continue;
                    }

                    for (String optionValue : optionValues) {
                        Option option = optionsHashMap.get(optionValue);
                        if (option == null) {
                            option = Option.builder().option(optionValue).question(question).build();
                            optionsHashMap.put(optionValue, option);
                        }

                        var correctOptionMatch = CorrectOptionMatch.builder()
                            .match(match)
                            .option(option)
                            .question(question)
                            .build();
                        correctOptionMatches.add(correctOptionMatch);
                    }
                }

                // normally correct option matches will save all
                // options and matches inside it
                question.setCorrectOptionMatches(correctOptionMatches);
                // but for standalone matches, we will save them
                // from the matches list
                question.setMatches(standaloneMatches);
                break;
        }
        return question;
    }


}
