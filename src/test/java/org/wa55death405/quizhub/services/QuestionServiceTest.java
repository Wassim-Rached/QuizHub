package org.wa55death405.quizhub.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.enums.QuestionType;
import org.wa55death405.quizhub.repositories.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private AnswerAttemptRepository answerAttemptRepository;
    @Mock
    private QuestionAttemptRepository questionAttemptRepository;
    @Mock
    private ChoiceAttemptRepository choiceAttemptRepository;
    @Mock
    private OrderedOptionAttemptRepository orderedOptionAttemptRepository;
    @Mock
    private OptionMatchAttemptRepository optionMatchAttemptRepository;
    @InjectMocks
    private QuestionService questionService;

    @Test
    public void testHandleQuestionAttempt_MultipleChoice_1() {
        // Setup
        var questionAttempt = createQuestionAttempt_MultipleChoice(
                QuestionType.MULTIPLE_CHOICE,
                Set.of(
                        Choice.builder().id(1).isCorrect(true).choice("7").build(),
                        Choice.builder().id(2).isCorrect(true).choice("13").build(),
                        Choice.builder().id(3).isCorrect(false).choice("4").build()
                ),
                Set.of(
                        ChoiceAttempt.builder().id(1).choice(Choice.builder().id(1).isCorrect(true).choice("7").build()).build(),
                        ChoiceAttempt.builder().id(2).choice(Choice.builder().id(2).isCorrect(true).choice("13").build()).build()
                )
        );

        // Mock behavior
        when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
        when(choiceAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getChoiceAttempts().stream().toList());

        // Call method
        questionService.handleQuestionAttempt(questionAttempt);

        // Assert correctnessPercentage is calculated
        var choices = questionAttempt.getChoiceAttempts().stream().toList();
        assertTrue(choices.get(0).getChoice().getIsCorrect());
        assertTrue(choices.get(1).getChoice().getIsCorrect());
        assertEquals(100F, questionAttempt.getCorrectnessPercentage());

        // Verify correctnessPercentage is calculated
        verify(questionAttemptRepository).save(any());
        verify(choiceAttemptRepository).saveAll(any());
    }

    @Test
    public void testHandleQuestionAttempt_MultipleChoice_2() {
        // Setup
        var questionAttempt = createQuestionAttempt_MultipleChoice(
                QuestionType.MULTIPLE_CHOICE,
                Set.of(
                        Choice.builder().id(1).isCorrect(true).choice("7").build(),
                        Choice.builder().id(2).isCorrect(true).choice("13").build(),
                        Choice.builder().id(3).isCorrect(false).choice("4").build()
                ),
                Set.of(
                        ChoiceAttempt.builder().id(1).choice(Choice.builder().id(1).isCorrect(true).choice("7").build()).build(),
                        ChoiceAttempt.builder().id(2).choice(Choice.builder().id(3).isCorrect(false).choice("4").build()).build()
                )
        );

        // Mock behavior
        when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
        when(choiceAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getChoiceAttempts().stream().toList());

        // Call method
        questionService.handleQuestionAttempt(questionAttempt);

        // Assert correctnessPercentage is calculated
        var choices = questionAttempt.getChoiceAttempts();
        assertEquals(1,choices.stream().filter(choiceAttempt -> choiceAttempt.getChoice().getIsCorrect()).count());
        assertEquals(1,choices.stream().filter(choiceAttempt -> !choiceAttempt.getChoice().getIsCorrect()).count());
        assertEquals(0F, questionAttempt.getCorrectnessPercentage());

        // Verify correctnessPercentage is calculated
        verify(questionAttemptRepository).save(any());
        verify(choiceAttemptRepository).saveAll(any());
    }

    @Test
    public void testHandleQuestionAttempt_TRUE_FALSE() {
        Question question = Question.builder()
                .id(1)
                .questionType(QuestionType.TRUE_FALSE)
                .question("is 7 a prime number?")
                .answer(Answer.builder().id(1).answer("true").build())
                .build();

        QuestionAttempt questionAttempt = QuestionAttempt.builder()
                .id(1)
                .question(question)
                .answerAttempt(AnswerAttempt.builder().id(1).answer("true").build())
                .build();

        // Mock behavior
        when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
        when(answerAttemptRepository.save(any())).thenReturn(questionAttempt.getAnswerAttempt());

        // Call method
        questionService.handleQuestionAttempt(questionAttempt);

        // Assert correctnessPercentage is calculated
        var answerAttempt = questionAttempt.getAnswerAttempt();
        assertTrue(answerAttempt.getIsCorrect());
        assertEquals(100F, questionAttempt.getCorrectnessPercentage());

        // Verify correctnessPercentage is calculated
        verify(questionAttemptRepository).save(any());
        verify(answerAttemptRepository).save(any());
    }

    @Test
    public void testHandleQuestionAttempt_SHORT_ANSWER() {
        Question question = Question.builder()
                .id(1)
                .questionType(QuestionType.SHORT_ANSWER)
                .question("what's the capital of Tunisia?")
                .answer(Answer.builder().id(1).answer("tunis").build())
                .build();

        QuestionAttempt questionAttempt = QuestionAttempt.builder()
                .id(1)
                .question(question)
                .answerAttempt(AnswerAttempt.builder().id(1).answer("tunis").build())
                .build();

        // Mock behavior
        when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
        when(answerAttemptRepository.save(any())).thenReturn(questionAttempt.getAnswerAttempt());

        // Call method
        questionService.handleQuestionAttempt(questionAttempt);

        // Assert correctnessPercentage is calculated
        var answerAttempt = questionAttempt.getAnswerAttempt();
        assertTrue(answerAttempt.getIsCorrect());
        assertEquals(100F, questionAttempt.getCorrectnessPercentage());

        // Verify correctnessPercentage is calculated
        verify(questionAttemptRepository).save(any());
        verify(answerAttemptRepository).save(any());
    }

    @Test
    public void testHandleQuestionAttempt_NUMERIC() {
        Question question = Question.builder()
                .id(1)
                .questionType(QuestionType.NUMERIC)
                .question("How many time zones are there in Russia?")
                .answer(Answer.builder().id(1).answer("11").build())
                .build();

        QuestionAttempt questionAttempt = QuestionAttempt.builder()
                .id(1)
                .question(question)
                .answerAttempt(AnswerAttempt.builder().id(1).answer("11").build())
                .build();

        // Mock behavior
        when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
        when(answerAttemptRepository.save(any())).thenReturn(questionAttempt.getAnswerAttempt());

        // Call method
        questionService.handleQuestionAttempt(questionAttempt);

        // Assert correctnessPercentage is calculated
        var answerAttempt = questionAttempt.getAnswerAttempt();
        assertTrue(answerAttempt.getIsCorrect());
        assertEquals(100F, questionAttempt.getCorrectnessPercentage());

        // Verify correctnessPercentage is calculated
        verify(questionAttemptRepository).save(any());
        verify(answerAttemptRepository).save(any());
    }

    @Test
    public void testHandleQuestionAttempt_FILL_IN_THE_BLANK() {
        Question question = Question.builder()
                .id(1)
                .questionType(QuestionType.FILL_IN_THE_BLANK)
                .question("do you [asser] me? do you do you, do you [asser] me? do you do you? do you [asser] me?")
                .answer(Answer.builder().id(1).answer("love;need;want").build())
                .build();

        QuestionAttempt questionAttempt = QuestionAttempt.builder()
                .id(1)
                .question(question)
                .answerAttempt(AnswerAttempt.builder().id(1).answer("love;need;want").build())
                .build();

        // Mock behavior
        when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
        when(answerAttemptRepository.save(any())).thenReturn(questionAttempt.getAnswerAttempt());

        // Call method
        questionService.handleQuestionAttempt(questionAttempt);

        // Assert correctnessPercentage is calculated
        var answerAttempt = questionAttempt.getAnswerAttempt();
        assertTrue(answerAttempt.getIsCorrect());
        assertEquals(100F, questionAttempt.getCorrectnessPercentage());

        // Verify correctnessPercentage is calculated
        verify(questionAttemptRepository).save(any());
        verify(answerAttemptRepository).save(any());
    }

    @Test
    public void testHandleQuestionAttempt_SINGLE_CHOICE() {
        Question question = Question.builder()
                .id(1)
                .questionType(QuestionType.SINGLE_CHOICE)
                .question("do you [asser] me? do you do you, do you [asser] me? do you do you? do you [asser] me?")
                .answer(Answer.builder().id(1).answer("love;need;want").build())
                .build();

        QuestionAttempt questionAttempt = QuestionAttempt.builder()
                .id(1)
                .question(question)
                .answerAttempt(AnswerAttempt.builder().id(1).answer("love;need;want").build())
                .build();

        // Mock behavior
        when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
        when(answerAttemptRepository.save(any())).thenReturn(questionAttempt.getAnswerAttempt());

        // Call method
        questionService.handleQuestionAttempt(questionAttempt);

        // Assert correctnessPercentage is calculated
        var answerAttempt = questionAttempt.getAnswerAttempt();
        assertTrue(answerAttempt.getIsCorrect());
        assertEquals(100F, questionAttempt.getCorrectnessPercentage());

        // Verify correctnessPercentage is calculated
        verify(questionAttemptRepository).save(any());
        verify(answerAttemptRepository).save(any());
    }

    @Test
    public void testHandleQuestionAttempt_OPTION_ORDERING_1() {
        Question question = Question.builder()
                .id(1)
                .questionType(QuestionType.OPTION_ORDERING)
                .question("put the following numbers in ascending order from top to bottom")
                .orderedOptions(
                        Set.of(
                                OrderedOption.builder().id(1).option("100").correctPosition(0).build(),
                                OrderedOption.builder().id(2).option("200").correctPosition(1).build(),
                                OrderedOption.builder().id(3).option("300").correctPosition(2).build()
                        )
                )
                .build();

        QuestionAttempt questionAttempt = QuestionAttempt.builder()
                .id(1)
                .question(question)
                .orderedOptionAttempts(
                        Set.of(
                                OrderedOptionAttempt.builder().id(1).sortedOption(OrderedOption.builder().id(1).option("100").correctPosition(0).build()).position(1).build(),
                                OrderedOptionAttempt.builder().id(2).sortedOption(OrderedOption.builder().id(2).option("200").correctPosition(1).build()).position(0).build(),
                                OrderedOptionAttempt.builder().id(3).sortedOption(OrderedOption.builder().id(3).option("300").correctPosition(2).build()).position(2).build()
                        )
                )
                .build();

        // Mock behavior
        when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
        when(orderedOptionAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getOrderedOptionAttempts().stream().toList());

        // Call method
        questionService.handleQuestionAttempt(questionAttempt);

        // Assert correctnessPercentage is calculated
        var orderedOptionAttempts = questionAttempt.getOrderedOptionAttempts().stream().toList();
        assertEquals(1,orderedOptionAttempts.stream().filter(OrderedOptionAttempt::getIsCorrect).count());
        assertEquals(2,orderedOptionAttempts.stream().filter(choiceAttempt -> !choiceAttempt.getIsCorrect()).count());
        assertEquals(0F, questionAttempt.getCorrectnessPercentage());

        // Verify correctnessPercentage is calculated
        verify(questionAttemptRepository).save(any());
        verify(orderedOptionAttemptRepository).saveAll(any());
    }

    @Test
    void testHandleQuestionAttempt_OPTION_ORDERING_2() {
        Question question = Question.builder()
                .id(1)
                .questionType(QuestionType.OPTION_ORDERING)
                .question("put the following numbers in ascending order from top to bottom")
                .orderedOptions(
                        Set.of(
                                OrderedOption.builder().id(1).option("100").correctPosition(0).build(),
                                OrderedOption.builder().id(2).option("200").correctPosition(1).build(),
                                OrderedOption.builder().id(3).option("300").correctPosition(2).build()
                        )
                )
                .build();

        QuestionAttempt questionAttempt = QuestionAttempt.builder()
                .id(1)
                .question(question)
                .orderedOptionAttempts(
                        Set.of(
                                OrderedOptionAttempt.builder().id(1).sortedOption(OrderedOption.builder().id(1).option("100").correctPosition(0).build()).position(0).build(),
                                OrderedOptionAttempt.builder().id(2).sortedOption(OrderedOption.builder().id(2).option("200").correctPosition(1).build()).position(1).build(),
                                OrderedOptionAttempt.builder().id(3).sortedOption(OrderedOption.builder().id(3).option("300").correctPosition(2).build()).position(2).build()
                        )
                )
                .build();

        // Mock behavior
        when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
        when(orderedOptionAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getOrderedOptionAttempts().stream().toList());

        // Call method
        questionService.handleQuestionAttempt(questionAttempt);

        // Assert correctnessPercentage is calculated
        var orderedOptionAttempts = questionAttempt.getOrderedOptionAttempts().stream().toList();
        assertEquals(3,orderedOptionAttempts.stream().filter(OrderedOptionAttempt::getIsCorrect).count());
        assertEquals(0,orderedOptionAttempts.stream().filter(choiceAttempt -> !choiceAttempt.getIsCorrect()).count());
        assertEquals(100F, questionAttempt.getCorrectnessPercentage());

        // Verify correctnessPercentage is calculated
        verify(questionAttemptRepository).save(any());
        verify(orderedOptionAttemptRepository).saveAll(any());
    }

    @Test
    void testHandleQuestionAttempt_OPTION_MATCHING_1() {
        // Options
        Option option_tunisia = Option.builder().id(1).option("Tunisia").build();
        Option option_france = Option.builder().id(2).option("France").build();
        Option option_usa = Option.builder().id(3).option("USA").build();
        // Matches
        Match match_tunis = Match.builder().id(1).match("Tunis").build();
        Match match_paris = Match.builder().id(2).match("Paris").build();
        Match match_washington = Match.builder().id(3).match("Washington").build();

        // question
        Question question = Question.builder()
                .id(1)
                .questionType(QuestionType.OPTION_MATCHING)
                .question("match the following countries with their capitals")
                .correctOptionMatches(
                        Set.of(
                                CorrectOptionMatch.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                CorrectOptionMatch.builder().id(2).option(option_france).match(match_paris).build(),
                                CorrectOptionMatch.builder().id(3).option(option_usa).match(match_washington).build()
                        )
                )
                .build();

        // attempt
        QuestionAttempt questionAttempt = QuestionAttempt.builder()
                .id(1)
                .question(question)
                .optionMatchAttempts(
                        Set.of(
                                OptionMatchAttempt.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                OptionMatchAttempt.builder().id(2).option(option_france).match(match_paris).build(),
                                OptionMatchAttempt.builder().id(3).option(option_usa).match(match_washington).build()
                        )
                )
                .build();

        // Mock behavior
        when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
        when(optionMatchAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getOptionMatchAttempts().stream().toList());

        // Call method
        questionService.handleQuestionAttempt(questionAttempt);

        // Assert correctnessPercentage is calculated
        var optionMatchAttempts = questionAttempt.getOptionMatchAttempts().stream().toList();
        assertEquals(3,optionMatchAttempts.stream().filter(OptionMatchAttempt::getIsCorrect).count());
        assertEquals(0,optionMatchAttempts.stream().filter(choiceAttempt -> !choiceAttempt.getIsCorrect()).count());
        assertEquals(100F, questionAttempt.getCorrectnessPercentage());

        // Verify correctnessPercentage is calculated
        verify(questionAttemptRepository).save(any());
        verify(optionMatchAttemptRepository).saveAll(any());
    }

    @Test
    void testHandleQuestionAttempt_OPTION_MATCHING_2() {
        // Options
        Option option_tunisia = Option.builder().id(1).option("Tunisia").build();
        Option option_france = Option.builder().id(2).option("France").build();
        Option option_usa = Option.builder().id(3).option("USA").build();
        // Matches
        Match match_tunis = Match.builder().id(1).match("Tunis").build();
        Match match_paris = Match.builder().id(2).match("Paris").build();
        Match match_washington = Match.builder().id(3).match("Washington").build();

        // question
        Question question = Question.builder()
                .id(1)
                .questionType(QuestionType.OPTION_MATCHING)
                .question("match the following countries with their capitals")
                .correctOptionMatches(
                        Set.of(
                                CorrectOptionMatch.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                CorrectOptionMatch.builder().id(2).option(option_france).match(match_paris).build(),
                                CorrectOptionMatch.builder().id(3).option(option_usa).match(match_washington).build()
                        )
                )
                .build();

        // attempt
        QuestionAttempt questionAttempt = QuestionAttempt.builder()
                .id(1)
                .question(question)
                .optionMatchAttempts(
                        Set.of(
                                OptionMatchAttempt.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                OptionMatchAttempt.builder().id(2).option(option_tunisia).match(match_paris).build(),
                                OptionMatchAttempt.builder().id(3).option(option_usa).match(match_washington).build()
                        )
                )
                .build();

        // Mock behavior
        when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
        when(optionMatchAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getOptionMatchAttempts().stream().toList());

        // Call method
        questionService.handleQuestionAttempt(questionAttempt);

        // Assert correctnessPercentage is calculated
        var optionMatchAttempts = questionAttempt.getOptionMatchAttempts().stream().toList();
        assertEquals(2,optionMatchAttempts.stream().filter(OptionMatchAttempt::getIsCorrect).count());
        assertEquals(1,optionMatchAttempts.stream().filter(choiceAttempt -> !choiceAttempt.getIsCorrect()).count());
        assertEquals(0F, questionAttempt.getCorrectnessPercentage());

        // Verify correctnessPercentage is calculated
        verify(questionAttemptRepository).save(any());
        verify(optionMatchAttemptRepository).saveAll(any());
    }
    /*
        [*] helper methods
    */
    private QuestionAttempt createQuestionAttempt_MultipleChoice(QuestionType questionType,Set<Choice> choices, Set<ChoiceAttempt> choiceAttempts) {
        Question question = Question.builder()
                .id(1)
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .question("which one of those are prime numbers?")
                .choices(choices)
                .build();

        return QuestionAttempt.builder()
                .id(1)
                .question(question)
                .choiceAttempts(choiceAttempts)
                .build();
    }
}
