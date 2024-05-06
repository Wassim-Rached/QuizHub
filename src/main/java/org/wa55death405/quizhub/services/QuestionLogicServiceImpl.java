package org.wa55death405.quizhub.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.enums.MultipleChoiceAlgorithmeType;
import org.wa55death405.quizhub.interfaces.services.IQuestionLogicService;
import org.wa55death405.quizhub.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionLogicServiceImpl implements IQuestionLogicService {
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
    @Override
    public void handleQuestionAttempt(QuestionAttempt questionAttempt) {
        switch (questionAttempt.getQuestion().getQuestionType()) {
            // "MULTIPLE_CHOICE" and "SINGLE_CHOICE" are handled differently
            // for performance reasons ,but they still share the same logic
            case MULTIPLE_CHOICE:
                handle_MULTIPLE_CHOICE(questionAttempt);
                break;
            case SINGLE_CHOICE:
                handle_SINGLE_CHOICE(questionAttempt);
                break;
            case TRUE_FALSE,FILL_IN_THE_BLANK,NUMERIC,SHORT_ANSWER:
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

    private void handle_SINGLE_CHOICE(QuestionAttempt questionAttempt) {
        List<ChoiceAttempt> choiceAttempts = questionAttempt.getChoiceAttempts() == null ? new ArrayList<>() : questionAttempt.getChoiceAttempts();
        ChoiceAttempt choiceAttempt = choiceAttempts.stream().findFirst().orElse(null);

        if (choiceAttempt == null || choiceAttempts.size() > 1) {
            questionAttempt.setCorrectnessPercentage(0F);
            questionAttempt.setChoiceAttempts(new ArrayList<>());
            questionAttemptRepository.save(questionAttempt);
            return;
        }

        Choice choice = choiceAttempt.getChoice();
        if (!questionAttempt.getQuestion().getChoices().contains(choice)) {
            questionAttempt.setCorrectnessPercentage(0F);
            questionAttempt.setChoiceAttempts(new ArrayList<>());
            questionAttemptRepository.save(questionAttempt);
            return;
        }

        if (choice.getIsCorrect()) {
            questionAttempt.setCorrectnessPercentage(100F);
            choiceAttempt.setIsCorrect(true);
        } else {
            questionAttempt.setCorrectnessPercentage(0F);
            choiceAttempt.setIsCorrect(false);
        }

        questionAttemptRepository.save(questionAttempt);
        choiceAttemptRepository.save(choiceAttempt);
    }

    private void handle_MULTIPLE_CHOICE(QuestionAttempt questionAttempt) {
        List<ChoiceAttempt> choiceAttempts = questionAttempt.getChoiceAttempts();
        List<Choice> choices = questionAttempt.getQuestion().getChoices();

        if (choiceAttempts == null || choiceAttempts.size() > choices.size()){
            choiceAttempts = new ArrayList<>();
            questionAttempt.setChoiceAttempts(choiceAttempts);
        }

        if (choiceAttempts.isEmpty()){
            questionAttempt.setCorrectnessPercentage(0F);
            questionAttemptRepository.save(questionAttempt);
            return;
        }

        int numberOfCorrectChoices = (int) choices.stream().filter(Choice::getIsCorrect).count();
        int correctAnswers = 0;
        int wrongAnswers = 0;

        for (var choiceAttempt : choiceAttempts) {
            if (!choices.contains(choiceAttempt.getChoice())) {
                questionAttempt.setChoiceAttempts(new ArrayList<>());
                questionAttempt.setCorrectnessPercentage(0F);
                questionAttemptRepository.save(questionAttempt);
                return;
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
        choiceAttemptRepository.saveAll(choiceAttempts);
    }

    private void handle_OPTION_ORDERING(QuestionAttempt questionAttempt) {
        List<OrderedOptionAttempt> orderedOptionAttempts = questionAttempt.getOrderedOptionAttempts();
        List<OrderedOption> orderedOptions = questionAttempt.getQuestion().getOrderedOptions();

        if (orderedOptionAttempts == null || orderedOptionAttempts.size() != orderedOptions.size()) {
            questionAttempt.setCorrectnessPercentage(0F);
            questionAttempt.setOrderedOptionAttempts(new ArrayList<>());
            questionAttemptRepository.save(questionAttempt);
            return;
        }

        boolean atLeastOneWrong = false;
        for (OrderedOptionAttempt orderedOptionAttempt : orderedOptionAttempts) {
            if (!orderedOptions.contains(orderedOptionAttempt.getOrderedOption())) {
                // i ain't dealing with unrelated options
                // just give the user a '0' 🗿.
                questionAttempt.setCorrectnessPercentage(0F);
                questionAttempt.setOrderedOptionAttempts(new ArrayList<>());
                questionAttemptRepository.save(questionAttempt);
                return;
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
        // this code I just wrote is one of the shittiest code I have ever written
        // its so bad may god forgive me and not let anyone see this crap
        // rip cpu, rip memory, rip query performance, rip everything
        // ~ Wassim Rached (27/08/2003 - 29/04/2024)
        List<OptionMatchAttempt> optionMatchAttempts = questionAttempt.getOptionMatchAttempts();
        List<CorrectOptionMatch> correctOptionMatches = questionAttempt.getQuestion().getCorrectOptionMatches();

        if (optionMatchAttempts == null || optionMatchAttempts.isEmpty()){
            questionAttempt.setOptionMatchAttempts(new ArrayList<>());
            questionAttempt.setCorrectnessPercentage(0F);
            questionAttemptRepository.save(questionAttempt);
            return;
        }

        int totalOptionMatchesAttempts = optionMatchAttempts.size();
        int totalOfOptions = questionAttempt.getQuestion().getOptions().size();
        int totalOfMatches = questionAttempt.getQuestion().getMatches().size();
        int maxPossibleMatches = totalOfOptions * totalOfMatches;

        if (totalOptionMatchesAttempts > maxPossibleMatches) {
            questionAttempt.setCorrectnessPercentage(0F);
            questionAttempt.setOptionMatchAttempts(new ArrayList<>());
            questionAttemptRepository.save(questionAttempt);
            return;
        }

        var questionOptions = questionAttempt.getQuestion().getOptions();
        var questionMatches = questionAttempt.getQuestion().getMatches();
        for (OptionMatchAttempt optionMatchAttempt : optionMatchAttempts) {
            Option option = optionMatchAttempt.getOption();
            Match match = optionMatchAttempt.getMatch();
            if (!questionOptions.contains(option) || !questionMatches.contains(match)) {
                questionAttempt.setCorrectnessPercentage(0F);
                questionAttempt.setOptionMatchAttempts(new ArrayList<>());
                questionAttemptRepository.save(questionAttempt);
                return;
            }

        }

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

        int totalCorrectOptionMatches = correctOptionMatches.size();
        if (correctMatches == totalCorrectOptionMatches) {
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

        if (attempt == null) {
            questionAttempt.setCorrectnessPercentage(0F);
            questionAttemptRepository.save(questionAttempt);
            return;
        }

        if (answer == null) {
            throw new IllegalArgumentException("Question with id " + questionAttempt.getQuestion().getId() + " has no answer");
        }
        
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
