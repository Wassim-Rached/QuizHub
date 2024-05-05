package org.wa55death405.quizhub.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.enums.QuestionType;
import org.wa55death405.quizhub.repositories.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FakeDataUtils {
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public Quiz fillFakeQuizData() {
        // create quiz
        Quiz quiz = new Quiz();
        quiz.setTitle("Capital Cities Quiz");
        quiz = quizRepository.save(quiz);

        /*
          create questions
         */

        //   TRUE_FALSE
        createFakeQuestion_general_comparison(QuestionType.TRUE_FALSE,quiz,"Is Russia The Biggest Country In The World?","true",1);
        //    SHORT_ANSWER
        createFakeQuestion_general_comparison(QuestionType.SHORT_ANSWER,quiz, "What is the capital of France?", "Paris",1);
        //    NUMERIC
        createFakeQuestion_general_comparison(QuestionType.NUMERIC,quiz, "How many countries are there in the world?", "195",1);
        //    FILL_IN_THE_BLANK
        createFakeQuestion_general_comparison(QuestionType.FILL_IN_THE_BLANK,quiz, "The capital of Italy is [asser],and the capital of France is [asser].", "Rome;Paris",1);

        //    MULTIPLE_CHOICE,
        createFakeQuestion_MULTIPLE_CHOICE(quiz, "which of the following are european countries?", List.of("USA","Canada"),List.of("France","Germany"),1);
        //    SINGLE_CHOICE,
        createFakeQuestion_SINGLE_CHOICE(quiz, "which of the following is a country in Africa?", List.of("USA","Canada","France"),"Nigeria",1);

        //    OPTION_ORDERING
        createFakeQuestion_OPTION_ORDERING(quiz, "Arrange the following countries in order of their population", List.of("China","India","USA","Indonesia"),1);

        //    OPTION_MATCHING
        createFakeQuestion_OPTION_MATCHING(quiz,"match countries with they're local languages",new HashMap<>(){{
            put("USA",List.of("English"));
            put("Canada",List.of("English","French"));
            put("France",List.of("French"));
        }},List.of("Arabic"),1);


        quiz = quizRepository.save(quiz);
        return quiz;
    }

    // FILL_IN_THE_BLANK,
    // SHORT_ANSWER,
    // TRUE_FALSE,
    // NUMERIC
    private void createFakeQuestion_general_comparison(QuestionType type,Quiz quiz, String question, String answer, float coefficient) {
        Question question1 = new Question();
        question1.setQuestion(question);
        question1.setQuestionType(type);
        question1.setQuiz(quiz);
        question1.setCoefficient(coefficient);
        questionRepository.save(question1);

        // create answers
        Answer answer1 = new Answer();
        answer1.setAnswer(answer);
        answer1.setQuestion(question1);
        answerRepository.save(answer1);
    }

    // MULTIPLE_CHOICE,
    private void createFakeQuestion_MULTIPLE_CHOICE(Quiz quiz, String question,List<String> wrongChoices, List<String> correctChoices, float coefficient) {
        List<Choice> choices = new ArrayList<>();

        Question question1 = Question.builder()
                .choices(choices)
                .question(question)
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .quiz(quiz)
                .coefficient(coefficient)
                .build();

        for (String choice : wrongChoices) {
            Choice choice1 = Choice.builder()
                    .choice(choice)
                    .isCorrect(false)
                    .question(question1)
                    .build();
            choices.add(choice1);
        }
        for (String choice : correctChoices) {
            Choice choice1 = Choice.builder()
                    .choice(choice)
                    .question(question1)
                    .isCorrect(true)
                    .build();
            choices.add(choice1);
        }

        questionRepository.save(question1);
    }

    // SINGLE_CHOICE
    private void createFakeQuestion_SINGLE_CHOICE(Quiz quiz,String question,List<String> wrongChoices, String correctChoice, float coefficient) {
        List<Choice> choices = new ArrayList<>();

        Question question1 = Question.builder()
                .choices(choices)
                .question(question)
                .questionType(QuestionType.SINGLE_CHOICE)
                .quiz(quiz)
                .coefficient(coefficient)
                .build();

        for (String choice : wrongChoices) {
            Choice choice1 = Choice.builder()
                    .choice(choice)
                    .isCorrect(false)
                    .question(question1)
                    .build();
            choices.add(choice1);
        }
            Choice correctChoice1 = Choice.builder()
                    .choice(correctChoice)
                    .question(question1)
                    .isCorrect(true)
                    .build();
            choices.add(correctChoice1);


        questionRepository.save(question1);
    }

    // OPTION_MATCHING
    private void createFakeQuestion_OPTION_MATCHING(Quiz quiz, String question, HashMap<String,List<String>> optionsWithMatches, List<String> extraMatches, float coefficient) {
        HashMap<String,Match> matches = new HashMap<String,Match>();
        List<Option> options = new ArrayList<>();
        List<CorrectOptionMatch> correctOptionMatches = new ArrayList<>();

        Question question1 = Question.builder()
                .options(options)
                .correctOptionMatches(correctOptionMatches)
                .question(question)
                .questionType(QuestionType.OPTION_MATCHING)
                .quiz(quiz)
                .coefficient(coefficient)
                .build();

        for (var entry: optionsWithMatches.entrySet()) {
            Option option = Option.builder()
                    .option(entry.getKey())
                    .question(question1)
                    .build();
            options.add(option);

            for (String match: entry.getValue()) {
                Match match1 = Match.builder()
                        .match(match)
                        .question(question1)
                        .build();

                if (matches.containsKey(match)) {
                    match1 = matches.get(match);
                } else {
                    matches.put(match, match1);
                }

                CorrectOptionMatch correctOptionMatch = CorrectOptionMatch.builder()
                        .option(option)
                        .match(match1)
                        .question(question1)
                        .build();
                correctOptionMatches.add(correctOptionMatch);
            }
        }

        extraMatches.forEach(match -> {
            Match match1 = Match.builder()
                    .match(match)
                    .question(question1)
                    .build();
            if (!matches.containsKey(match)) {
                matches.put(match, match1);
            }
        });

        System.out.println(matches.size());
        question1.setMatches(new ArrayList<>(matches.values()));

        questionRepository.save(question1);
    }

    // OPTION_ORDERING
    private void createFakeQuestion_OPTION_ORDERING(Quiz quiz, String question, List<String> orderedOptions,  float coefficient) {
            List<OrderedOption> orderedOptions1 = new ArrayList<>();

            Question question1 = Question.builder()
                    .orderedOptions(orderedOptions1)
                    .question(question)
                    .questionType(QuestionType.OPTION_ORDERING)
                    .quiz(quiz)
                    .coefficient(coefficient)
                    .build();

            int index = 0;
            for (var option: orderedOptions) {
                OrderedOption orderedOption = OrderedOption.builder()
                        .option(option)
                        .correctPosition(index++)
                        .question(question1)
                        .build();
                orderedOptions1.add(orderedOption);
            }

            questionRepository.save(question1);
    }

}
