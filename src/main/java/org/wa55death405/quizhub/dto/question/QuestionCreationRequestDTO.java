package org.wa55death405.quizhub.dto.question;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.enums.QuestionType;
import org.wa55death405.quizhub.exceptions.InputValidationException;
import org.wa55death405.quizhub.interfaces.dto.EntityDTO;

import java.util.*;

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
        if (this.coefficient <= 0) throw new InputValidationException("Coefficient must be greater than 0 for question '" + this.question + "'");
        if (this.coefficient > Question.MAX_COEFFICIENT) throw new InputValidationException("Coefficient must be less than or equal to " + Question.MAX_COEFFICIENT + " for question '" + this.question + "'");

        if (this.question == null || this.question.isBlank()) throw new InputValidationException("Question is required and cannot be empty");
        if (this.questionType == null) throw new InputValidationException("Question type is required for question '" + this.question + "'");
        if (quiz == null) throw new InputValidationException("Quiz is required for question '" + this.question + "'");

        Question question = new Question();
        question.setQuestion(this.question);
        question.setQuestionType(this.questionType);
        question.setCoefficient(coefficient);
        question.setQuiz(quiz);

        switch (questionType) {
            case TRUE_FALSE,SHORT_ANSWER,NUMERIC,FILL_IN_THE_BLANK:
                this.generalComparison(question);
                break;

            case MULTIPLE_CHOICE:
            case SINGLE_CHOICE:
                this.choiceType(question);
                break;

            case OPTION_ORDERING:
                this.optionOrdering(question);
                break;

            case OPTION_MATCHING:
                this.optionMatching(question);
                break;
        }
        return question;
    }

    // private helpers
    private void generalComparison(Question question){
        if (this.questionType == QuestionType.FILL_IN_THE_BLANK) {
            if (this.paragraphToBeFilled == null || this.paragraphToBeFilled.isBlank()) {
                throw new InputValidationException("Paragraph to be filled is required for question '" + this.question + "' of type " + this.questionType);
            }

            int blanksCount = Question.countBlanks(this.paragraphToBeFilled);
            if (blanksCount < Question.MIN_FILL_IN_THE_BLANK_BLANKS || blanksCount > Question.MAX_FILL_IN_THE_BLANK_BLANKS) {
                throw new InputValidationException("Paragraph to be filled must contain at least one blank for question '" + this.question + "' of type " + this.questionType);
            }
        }

        // this is between both FILL_IN_THE_BLANK, so it verifies the existence
        // of the answer before counting it
        if (this.answers == null || this.answers.isEmpty()) {
            throw new InputValidationException("Answer is required for question '" + this.question + "' of type " + this.questionType);
        }

        if (this.questionType == QuestionType.FILL_IN_THE_BLANK) {
            int blanksCount = Question.countBlanks(this.paragraphToBeFilled);
            int answersCount = Question.countBlanksAnswers(this.answers.get(0));
            if (blanksCount != answersCount){
                throw new InputValidationException("There are " + blanksCount + " blanks in the paragraph but " + answersCount + " answers were provided for question '" + this.question + "' of type " + this.questionType);
            }

            question.setParagraphToBeFilled(this.paragraphToBeFilled);
        }

        question.setAnswers(
                this.answers.stream()
                        .map(answer -> Answer.builder().answer(answer).question(question).build())
                        .toList()
        );
    }

    private void choiceType(Question question){
        if (this.choices == null || this.choices.isEmpty()) {
            throw new InputValidationException("Choices are required for question '" + this.question + "' of type " + this.questionType);
        }

        if (this.choices.size() < Question.MIN_NUMBER_OF_CHOICES || this.choices.size() > Question.MAX_NUMBER_OF_CHOICES) {
            throw new InputValidationException("Number of choices must be between " + Question.MIN_NUMBER_OF_CHOICES + " and " + Question.MAX_NUMBER_OF_CHOICES + " for question '" + this.question + "' of type " + this.questionType);
        }

        if (this.questionType == QuestionType.SINGLE_CHOICE) {
            if (this.choices.values().stream().filter(Boolean::booleanValue).count() != 1) {
                throw new InputValidationException("Exactly one choice must be correct for question '" + this.question + "' of type " + this.questionType);
            }
        }

        if (this.questionType == QuestionType.MULTIPLE_CHOICE){
            if (this.choices.values().stream().noneMatch(Boolean::booleanValue)) {
                throw new InputValidationException("At least one choice must be correct for question '" + this.question + "' of type " + this.questionType);
            }
        }

        var choicesList = this.choices.entrySet().stream()
                .map(entry -> Choice.builder().choice(entry.getKey()).isCorrect(entry.getValue()).question(question).build()).toList();
        question.setChoices(choicesList);
    }

    private void optionOrdering(Question question){
        if (this.orderedOptions == null || this.orderedOptions.isEmpty()) {
            throw new InputValidationException("Ordered options are required for question '" + this.question + "' of type " + this.questionType);
        }
        int numberOfOrderedOptions = this.orderedOptions.size();
        if (numberOfOrderedOptions < Question.MIN_NUMBER_OF_ORDERED_OPTIONS || numberOfOrderedOptions > Question.MAX_NUMBER_OF_ORDERED_OPTIONS) {
            throw new InputValidationException("Number of ordered options must be between " + Question.MIN_NUMBER_OF_ORDERED_OPTIONS + " and " + Question.MAX_NUMBER_OF_ORDERED_OPTIONS + " for question '" + this.question + "' of type " + this.questionType);
        }

        var orderedOptions = new ArrayList<OrderedOption>();
        for (var entry : this.orderedOptions.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isBlank()) {
                throw new InputValidationException("Option value cannot be empty for question '" + this.question + "' of type " + this.questionType + " at position " + entry.getKey());
            }
            orderedOptions.add(OrderedOption.builder()
                    .correctPosition(entry.getKey())
                    .option(entry.getValue())
                    .question(question)
                    .build());
        }
        question.setOrderedOptions(orderedOptions);
    }

    private void optionMatching(Question question){
        // we don't check for duplication because the data is received as a map
        if (this.optionMatches == null || this.optionMatches.isEmpty()) {
            throw new InputValidationException("options and matches are required for question '" + this.question + "' of type " + this.questionType);
        }

        int numberOfOptionMatchesMatches = this.numberOfMatches();
        if (numberOfOptionMatchesMatches < Question.MIN_NUMBER_OF_OPTION_MATCHES_MATCHES || numberOfOptionMatchesMatches > Question.MAX_NUMBER_OF_OPTION_MATCHES_MATCHES) {
            throw new InputValidationException("Number of matches must be between " + Question.MIN_NUMBER_OF_OPTION_MATCHES_MATCHES + " and " + Question.MAX_NUMBER_OF_OPTION_MATCHES_MATCHES + " for question '" + this.question + "' of type " + this.questionType);
        }

        int numberOfOptionMatchesOptions = this.numberOfOptions();
        if (numberOfOptionMatchesOptions < Question.MIN_NUMBER_OF_OPTION_MATCHES_OPTIONS || numberOfOptionMatchesOptions > Question.MAX_NUMBER_OF_OPTION_MATCHES_OPTIONS) {
            throw new InputValidationException("Number of options must be between " + Question.MIN_NUMBER_OF_OPTION_MATCHES_OPTIONS + " and " + Question.MAX_NUMBER_OF_OPTION_MATCHES_OPTIONS + " for question '" + this.question + "' of type " + this.questionType);
        }

        // algorithm for transforming the data
        var correctOptionMatches = new ArrayList<CorrectOptionMatch>();
        var standaloneMatches = new ArrayList<Match>();
        var optionsHashMap = new HashMap<String,Option>();
        for (var entry : this.optionMatches.entrySet()) {
            List<String> optionValues = entry.getValue();
            String matchValue = entry.getKey();
            if (matchValue == null || matchValue.isBlank()) {
                throw new InputValidationException("Match value cannot be empty for question '" + this.question + "' of type " + this.questionType);
            }
            var match = Match.builder().match(matchValue).question(question).build();

            if (optionValues == null || optionValues.isEmpty()) {
                standaloneMatches.add(match);
                continue;
            }

            for (String optionValue : optionValues) {
                if (optionValue == null || optionValue.isBlank()) {
                    throw new InputValidationException("Option value cannot be empty for question '" + this.question + "' of type " + this.questionType);
                }
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
    }

    private String[] getOptions(){
        Set<String> options = new HashSet<>();
        this.optionMatches.values().forEach(options::addAll);
        return options.toArray(new String[0]);
    }

    // global helpers
    public int numberOfOptions(){
        return this.getOptions().length;
    }

    public int numberOfMatches(){
        return this.optionMatches.size();
    }

}
