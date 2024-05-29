package org.wa55death405.quizhub.utils;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.dto.questionAttempt.QuestionAttemptSubmissionDTO;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.exceptions.IrregularBehaviorException;
import org.wa55death405.quizhub.interfaces.utils.IFakeDataLogicalGenerator;

import java.util.*;
import java.util.stream.Collectors;

/*
    * This class is responsible for generating fake data
    not to be mistaken with 'FakeDataRandomGeneratorImpl'
    * this class is used to generate fake data
    that requires logical generations
 */
@Service
@RequiredArgsConstructor
public class FakeDataLogicalGeneratorImpl implements IFakeDataLogicalGenerator {

    @Override
    public QuestionAttemptSubmissionDTO getPerfectScoreQuestionAttemptSubmissionDTO(Question question) {
        /*
            I don't know what tf did I just wrote, but it works
            it returns a QuestionAttemptSubmissionDTO
            with all the correct answers for the given question
         */
        QuestionAttemptSubmissionDTO questionAttemptSubmissionDTO = new QuestionAttemptSubmissionDTO();
        questionAttemptSubmissionDTO.setQuestion(question.getId());
        switch (question.getQuestionType()){
            case TRUE_FALSE,NUMERIC,SHORT_ANSWER,FILL_IN_THE_BLANK:
                questionAttemptSubmissionDTO.setAnswerAttempt(question.getAnswer().getAnswer());
                break;
            case MULTIPLE_CHOICE,SINGLE_CHOICE:
                questionAttemptSubmissionDTO.setChoiceAttempts(question.getCorrectChoices().stream().map(Choice::getId).toList());
                break;
            case OPTION_ORDERING: questionAttemptSubmissionDTO.setOrderedOptionAttempts((HashMap<Integer, Integer>) question.getOrderedOptions().stream()
                    .collect(Collectors.toMap(OrderedOption::getCorrectPosition,OrderedOption::getId)));
            break;
            case OPTION_MATCHING:
                HashMap<Integer,List<Integer>> optionMatchAttempts = new HashMap<>();
                question.getCorrectOptionMatches().forEach(correctOptionMatch -> {
                    Integer optionId = correctOptionMatch.getOption().getId();
                    Integer matchId = correctOptionMatch.getMatch().getId();
                    var optionMatchAttempt = optionMatchAttempts.computeIfAbsent(optionId, k -> new ArrayList<>());
                    optionMatchAttempt.add(matchId);
                });
                questionAttemptSubmissionDTO.setOptionMatchAttempts(optionMatchAttempts);
            break;
            default: {
                throw new IrregularBehaviorException("Invalid Question Type");
            }
        }
        return questionAttemptSubmissionDTO;
    }

    @Override
    public List<QuestionAttemptSubmissionDTO> getPerfectScoreQuestionAttemptSubmissionDTOsForQuiz(Quiz quiz) {
        return quiz.getQuestions().stream().map(this::getPerfectScoreQuestionAttemptSubmissionDTO).toList();
    }

    // TODO : the logic might not be the best here
    @Override
    public QuestionAttemptSubmissionDTO getRandomQuestionAttemptSubmissionDTO(Question question) {
        QuestionAttemptSubmissionDTO questionAttemptSubmissionDTO = new QuestionAttemptSubmissionDTO();
        questionAttemptSubmissionDTO.setQuestion(question.getId());
        switch (question.getQuestionType()) {
            case TRUE_FALSE, NUMERIC, SHORT_ANSWER, FILL_IN_THE_BLANK:{
                // one in 3 chance of getting the correct answer
                if (Faker.instance().random().nextInt(1,5) == 1) {
                    questionAttemptSubmissionDTO.setAnswerAttempt(question.getAnswer().getAnswer());
                } else {
                    questionAttemptSubmissionDTO.setAnswerAttempt(Faker.instance().lorem().word());
                }
                break;
            }
            case MULTIPLE_CHOICE, SINGLE_CHOICE:{
                var questionChoices = question.getChoices();
                var randomChoices = new ArrayList<UUID>();
                for (Choice questionChoice : questionChoices) {
                    if (Faker.instance().bool().bool()) {
                        randomChoices.add(questionChoice.getId());
                    }
                }
                questionAttemptSubmissionDTO.setChoiceAttempts(randomChoices);
                break;
            }
            case OPTION_ORDERING:{
                var randomizedOrderedOptions = new ArrayList<>(question.getOrderedOptions().stream().map(OrderedOption::getId).toList());
                Collections.shuffle(randomizedOrderedOptions);
                var orderedOptionAttempts = new HashMap<Integer,Integer>();
                for (int i = 0; i < randomizedOrderedOptions.size(); i++) {
                    orderedOptionAttempts.put(i,randomizedOrderedOptions.get(i));
                }
                questionAttemptSubmissionDTO.setOrderedOptionAttempts(orderedOptionAttempts);
                break;
            }
            case OPTION_MATCHING:{
                // pick a random number of option matches which will be
                // between the number of options and the product of the number of options and the number of matches
                var numberOfOptions = question.getOptions().size();
                var numberOfMatches = question.getMatches().size();
                var numberOfOptionMatchesToMake = Faker.instance().random().nextInt(numberOfOptions,numberOfOptions*numberOfMatches);

                // shuffle the options and matches
                var randomizedOptionsIds = new ArrayList<>(question.getOptions().stream().map(Option::getId).toList());
                var randomizedMatchesIds = new ArrayList<>(question.getMatches().stream().map(Match::getId).toList());
                Collections.shuffle(randomizedOptionsIds);
                Collections.shuffle(randomizedMatchesIds);

                // <optionId, List<matchId>>
                var randomOptionMatchesAttempts = new HashMap<Integer,List<Integer>>();

                // loop through the number of option matches to make
                for (int i = 0; i < numberOfOptionMatchesToMake; i++) {
                    // get a random option and match
                    var optionId = randomizedOptionsIds.get(Faker.instance().random().nextInt(numberOfOptions-1));
                    var matchId = randomizedMatchesIds.get(Faker.instance().random().nextInt(numberOfMatches-1));
                    // connect the option to the match
                    if (randomOptionMatchesAttempts.containsKey(optionId)) {
                        randomOptionMatchesAttempts.get(optionId).add(matchId);
                    } else {
                        randomOptionMatchesAttempts.put(optionId, new ArrayList<>(List.of(matchId)));
                    }
                }
                // set the option matches
                questionAttemptSubmissionDTO.setOptionMatchAttempts(randomOptionMatchesAttempts);
                break;
            }
            default: {
                throw new IrregularBehaviorException("Invalid Question Type");
            }
        }
        return questionAttemptSubmissionDTO;
    }

    @Override
    public List<QuestionAttemptSubmissionDTO> getRandomQuestionAttemptSubmissionDTOsForQuiz(Quiz quiz) {
        return quiz.getQuestions().stream().map(this::getRandomQuestionAttemptSubmissionDTO).toList();
    }

    @Override
    public QuizAttempt generate_QuizAttempt(Quiz quiz){
        var attempts = this.getRandomQuestionAttemptSubmissionDTOsForQuiz(quiz);
        var quizAttempt = QuizAttempt.builder().quiz(quiz).build();
        List<QuestionAttempt> questionAttempts = attempts.stream().map(a->a.toEntity(quizAttempt.getId())).toList();
        quizAttempt.setQuestionAttempts(questionAttempts);
        return quizAttempt;
    }

}
