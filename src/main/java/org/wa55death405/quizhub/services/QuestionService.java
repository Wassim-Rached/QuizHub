package org.wa55death405.quizhub.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.enums.MultipleChoiceAlgorithmeType;
import org.wa55death405.quizhub.repositories.AnswerAttemptRepository;
import org.wa55death405.quizhub.repositories.ChoiceAttemptRepository;
import org.wa55death405.quizhub.repositories.QuestionAttemptRepository;

import java.util.Arrays;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final AnswerAttemptRepository answerAttemptRepository;
    private final QuestionAttemptRepository questionAttemptRepository;
    private final ChoiceAttemptRepository choiceAttemptRepository;

    // this method is called when a user attempts a question
    // its core responsibility is to determine if the user's answer is correct
    // therefor it sets the correctness percentage of the question attempt
    // additionally it sets fields for the attempt(s) of the question
    public void handleQuestionAttempt(QuestionAttempt questionAttempt) {
        switch (questionAttempt.getQuestion().getQuestionType()) {
            case MULTIPLE_CHOICE:
                handle_MULTIPLE_CHOICE(questionAttempt);
                break;
            case TRUE_FALSE,FILL_IN_THE_BLANK,SINGLE_CHOICE,NUMERIC,SHORT_ANSWER:
                general_comparison(questionAttempt);
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
