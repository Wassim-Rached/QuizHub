package org.wa55death405.quizhub.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.enums.MultipleChoiceAlgorithmeType;
import org.wa55death405.quizhub.repositories.*;

import java.util.Iterator;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final AnswerAttemptRepository answerAttemptRepository;
    private final QuestionAttemptRepository questionAttemptRepository;
    private final ChoiceAttemptRepository choiceAttemptRepository;
    private final OrderedOptionAttemptRepository orderedOptionAttemptRepository;
    private final OptionMatchAttemptRepository optionMatchAttemptRepository;

    // this method is called when a user attempts a question
    // its core responsibility is to determine if the user's answer is correct
    // therefor it sets the correctness percentage of the question attempt
    // additionally it sets the correctness of the various answer attempts
    // wherever the answer be [ChoiceAttempt, OrderedOptionAttempt,AnswerAttempt...]
    public void handleQuestionAttempt(QuestionAttempt questionAttempt) {
        switch (questionAttempt.getQuestion().getQuestionType()) {
            case MULTIPLE_CHOICE:
                handle_MULTIPLE_CHOICE(questionAttempt);
                break;
            case TRUE_FALSE,FILL_IN_THE_BLANK,SINGLE_CHOICE,NUMERIC,SHORT_ANSWER:
                general_comparison(questionAttempt);
                break;
            case OPTION_ORDERING:
                handle_OPTION_ORDERING(questionAttempt);
                break;
            case OPTION_MATCHING:
                handle_OPTION_MATCHING(questionAttempt);
                break;
            default:
                throw new IllegalArgumentException("Invalid question type");
        }
    }

    private void handle_MULTIPLE_CHOICE(QuestionAttempt questionAttempt) {
        Set<ChoiceAttempt> answerAttempts = questionAttempt.getChoiceAttempts();
        Set<Choice> choices = questionAttempt.getQuestion().getChoices();
        int numberOfCorrectChoices = (int) choices.stream().filter(Choice::getIsCorrect).count();
        int correctAnswers = 0;
        int wrongAnswers = 0;
        for (ChoiceAttempt choiceAttempt : answerAttempts) {
            if (!choices.contains(choiceAttempt.getChoice())) {
                throw new IllegalArgumentException("Choice with id " + choiceAttempt.getChoice().getId() + " is not part of the question");
            }

            Choice choice = choiceAttempt.getChoice();
            if (choice.getIsCorrect()){
                correctAnswers++;
                choiceAttempt.setIsCorrect(true);
            }else {
                wrongAnswers++;
                choiceAttempt.setIsCorrect(false);
            }
        }

        questionAttempt.setCorrectnessPercentage(calculate_score_for_MULTIPLE_CHOICE(
                MultipleChoiceAlgorithmeType.ALL_OR_NOTHING,
                correctAnswers,
                wrongAnswers,
                choices.size(),
                numberOfCorrectChoices
        ));

        questionAttemptRepository.save(questionAttempt);
        choiceAttemptRepository.saveAll(answerAttempts);
    }

    private void handle_OPTION_ORDERING(QuestionAttempt questionAttempt) {
        Set<OrderedOptionAttempt> orderedOptionAttempts = questionAttempt.getOrderedOptionAttempts();
        Set<OrderedOption> orderedOptions = questionAttempt.getQuestion().getOrderedOptions();

        boolean atLeastOneWrong = false;
        for (OrderedOptionAttempt orderedOptionAttempt : orderedOptionAttempts) {
            if (!orderedOptions.contains(orderedOptionAttempt.getSortedOption())) {
                throw new IllegalArgumentException("OrderedOption with id " + orderedOptionAttempt.getSortedOption().getId() + " is not part of the question");
            }
            if(!orderedOptionAttempt.validate()){
                atLeastOneWrong = true;
            }
        }

        if (atLeastOneWrong){
            questionAttempt.setCorrectnessPercentage(0F);
        }else {
            questionAttempt.setCorrectnessPercentage(100F);
        }

        questionAttemptRepository.save(questionAttempt);
        orderedOptionAttemptRepository.saveAll(orderedOptionAttempts);
    }

    private void handle_OPTION_MATCHING(QuestionAttempt questionAttempt) {
        // this code i just wrote is one of the shittiest code i have ever written
        // its so bad may god forgive me and not let me anyone see this
        // rip cpu, rip memory, rip query performance, rip everything
        // ~ Wassim Rached (27/08/2003 - 29/04/2024)
        Set<OptionMatchAttempt> optionMatchAttempts = questionAttempt.getOptionMatchAttempts();
        Set<CorrectOptionMatch> correctOptionMatches = questionAttempt.getQuestion().getCorrectOptionMatches();

        int correctMatches = 0;
        for (CorrectOptionMatch correctOptionMatch : correctOptionMatches) {
                for (OptionMatchAttempt optionMatchAttempt : optionMatchAttempts) {
                    if (correctOptionMatch.verifyAttempt(optionMatchAttempt)) {
                        correctMatches++;
                        optionMatchAttempt.setIsCorrect(true);
                        break;
                    }
                    if(optionMatchAttempt.getIsCorrect() == null){
                        optionMatchAttempt.setIsCorrect(false);
                    }
                }
        }
        int totalMatches = correctOptionMatches.size();
        if (correctMatches == totalMatches && optionMatchAttempts.size() == totalMatches) {
            questionAttempt.setCorrectnessPercentage(100F);
        } else {
            questionAttempt.setCorrectnessPercentage(0F);
        }

        questionAttemptRepository.save(questionAttempt);
        optionMatchAttemptRepository.saveAll(optionMatchAttempts);
    }

    private void general_comparison(QuestionAttempt questionAttempt) {
        AnswerAttempt attempt = questionAttempt.getAnswerAttempt();
        Answer answer = questionAttempt.getQuestion().getAnswer();
        
        if (answer.compareAnswer(attempt.getAnswer())){
            questionAttempt.setCorrectnessPercentage(100F);
            attempt.setIsCorrect(true);
        } else {
            questionAttempt.setCorrectnessPercentage(0F);
            attempt.setIsCorrect(false);
        }
        answerAttemptRepository.save(attempt);
        questionAttemptRepository.save(questionAttempt);
    }

    private Float calculate_score_for_MULTIPLE_CHOICE(MultipleChoiceAlgorithmeType type, int correctAnswers, int wrongAnswers, int totalChoices, int totalCorrectChoices) {
        // this might be needed when diffrent algorithmes are implemented
        // for calculating the score of a multiple choice question
        return switch (type) {
            case PERCENTAGE_OF_CORRECT_ANSWERS_FROM_ALL_CHOICES, PERCENTAGE_OF_CORRECT_ANSWERS_FROM_CORRECT_CHOICES ->
                    // currently not implemented
                    throw new UnsupportedOperationException("Not implemented yet");
            case ALL_OR_NOTHING -> {
                if (correctAnswers == totalCorrectChoices && wrongAnswers == 0) {
                    yield 100F;
                }
                yield 0F;
            }
            default -> throw new IllegalArgumentException("Invalid algorithme type");
        };
    }

}
