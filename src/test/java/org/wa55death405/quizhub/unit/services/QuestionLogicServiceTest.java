package org.wa55death405.quizhub.unit.services;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wa55death405.quizhub.entities.*;
import org.wa55death405.quizhub.enums.QuestionType;
import org.wa55death405.quizhub.repositories.*;
import org.wa55death405.quizhub.services.QuestionLogicServiceImpl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class QuestionLogicServiceTest {
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
    private QuestionLogicServiceImpl questionLogicService;

    @Nested
    class Test_OPTION_MATCHING{
        // normal behavior tests
        @Test
        void testHandleQuestionAttempt_normal() {
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
                            List.of(
                                    CorrectOptionMatch.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                    CorrectOptionMatch.builder().id(2).option(option_france).match(match_paris).build(),
                                    CorrectOptionMatch.builder().id(3).option(option_usa).match(match_washington).build()
                            )
                    )
                    .options(List.of(option_tunisia, option_france, option_usa))
                    .matches(List.of(match_tunis, match_paris, match_washington))
                    .build();

            // attempt
            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .optionMatchAttempts(
                            List.of(
                                    OptionMatchAttempt.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                    OptionMatchAttempt.builder().id(2).option(option_france).match(match_paris).build(),
                                    OptionMatchAttempt.builder().id(3).option(option_usa).match(match_washington).build()
                            )
                    )
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
            when(optionMatchAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getOptionMatchAttempts());

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var optionMatchAttempts = questionAttempt.getOptionMatchAttempts();
            assertEquals(3,optionMatchAttempts.stream().filter(OptionMatchAttempt::getIsCorrect).count());
            assertEquals(0,optionMatchAttempts.stream().filter(choiceAttempt -> !choiceAttempt.getIsCorrect()).count());
            assertEquals(100F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verify(optionMatchAttemptRepository).saveAll(any());
        }

        @Test
        void testHandleQuestionAttempt_part_failure() {
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
                            List.of(
                                    CorrectOptionMatch.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                    CorrectOptionMatch.builder().id(2).option(option_france).match(match_paris).build(),
                                    CorrectOptionMatch.builder().id(3).option(option_usa).match(match_washington).build()
                            )
                    )
                    .options(List.of(option_tunisia, option_france, option_usa))
                    .matches(List.of(match_tunis, match_paris, match_washington))
                    .build();

            // attempt
            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .optionMatchAttempts(
                            List.of(
                                    OptionMatchAttempt.builder().id(1).option(option_tunisia).match(match_paris).build(),
                                    OptionMatchAttempt.builder().id(2).option(option_france).match(match_tunis).build(),
                                    OptionMatchAttempt.builder().id(3).option(option_usa).match(match_washington).build()
                            )
                    )
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
            when(optionMatchAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getOptionMatchAttempts());

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var optionMatchAttempts = questionAttempt.getOptionMatchAttempts();
            assertEquals(1,optionMatchAttempts.stream().filter(OptionMatchAttempt::getIsCorrect).count());
            assertEquals(2,optionMatchAttempts.stream().filter(choiceAttempt -> !choiceAttempt.getIsCorrect()).count());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verify(optionMatchAttemptRepository).saveAll(any());
        }

        @Test
        void testHandleQuestionAttempt_failure() {
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
                            List.of(
                                    CorrectOptionMatch.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                    CorrectOptionMatch.builder().id(2).option(option_france).match(match_paris).build(),
                                    CorrectOptionMatch.builder().id(3).option(option_usa).match(match_washington).build()
                            )
                    )
                    .options(List.of(option_tunisia, option_france, option_usa))
                    .matches(List.of(match_tunis, match_paris, match_washington))
                    .build();

            // attempt
            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .optionMatchAttempts(
                            List.of(
                                    OptionMatchAttempt.builder().id(1).option(option_tunisia).match(match_washington).build(),
                                    OptionMatchAttempt.builder().id(2).option(option_france).match(match_tunis).build(),
                                    OptionMatchAttempt.builder().id(3).option(option_usa).match(match_paris).build()
                            )
                    )
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
            when(optionMatchAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getOptionMatchAttempts());

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var optionMatchAttempts = questionAttempt.getOptionMatchAttempts();
            assertEquals(0,optionMatchAttempts.stream().filter(OptionMatchAttempt::getIsCorrect).count());
            assertEquals(3,optionMatchAttempts.stream().filter(choiceAttempt -> !choiceAttempt.getIsCorrect()).count());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verify(optionMatchAttemptRepository).saveAll(any());
        }

        // unpredicted behavior tests
        @Test
        void testHandleQuestionAttempt_unrelated_option_match() {
            // Options
            Option option_tunisia = Option.builder().id(1).option("Tunisia").build();
            Option option_france = Option.builder().id(2).option("France").build();
            Option option_usa = Option.builder().id(3).option("USA").build();
            Option option_tomato = Option.builder().id(44).option("Tomato").build();
            // Matches
            Match match_tunis = Match.builder().id(1).match("Tunis").build();
            Match match_paris = Match.builder().id(2).match("Paris").build();
            Match match_washington = Match.builder().id(3).match("Washington").build();
            Match match_fruit = Match.builder().id(44).match("Fruit").build();

            // question
            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.OPTION_MATCHING)
                    .question("match the following countries with their capitals")
                    .correctOptionMatches(
                            List.of(
                                    CorrectOptionMatch.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                    CorrectOptionMatch.builder().id(2).option(option_france).match(match_paris).build(),
                                    CorrectOptionMatch.builder().id(3).option(option_usa).match(match_washington).build()
                            )
                    )
                    .options(List.of(option_tunisia, option_france, option_usa))
                    .matches(List.of(match_tunis, match_paris, match_washington))
                    .build();

            // attempt
            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .optionMatchAttempts(
                            List.of(
                                    OptionMatchAttempt.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                    OptionMatchAttempt.builder().id(4).option(option_tomato).match(match_fruit).build()
                            )
                    )
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var optionMatchAttempts = questionAttempt.getOptionMatchAttempts();
            assertEquals(0,optionMatchAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(optionMatchAttemptRepository);
        }

        @Test
        void testHandleQuestionAttempt_overflow() {
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
                            List.of(
                                    CorrectOptionMatch.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                    CorrectOptionMatch.builder().id(2).option(option_france).match(match_paris).build(),
                                    CorrectOptionMatch.builder().id(3).option(option_usa).match(match_washington).build()
                            )
                    )
                    .options(List.of(option_tunisia, option_france, option_usa))
                    .matches(List.of(match_tunis, match_paris, match_washington))
                    .build();

            // attempt
            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .optionMatchAttempts(
                            List.of(
                                    OptionMatchAttempt.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                    OptionMatchAttempt.builder().id(2).option(option_tunisia).match(match_paris).build(),
                                    OptionMatchAttempt.builder().id(3).option(option_tunisia).match(match_washington).build(),

                                    OptionMatchAttempt.builder().id(4).option(option_france).match(match_tunis).build(),
                                    OptionMatchAttempt.builder().id(5).option(option_france).match(match_paris).build(),
                                    OptionMatchAttempt.builder().id(6).option(option_france).match(match_washington).build(),

                                    OptionMatchAttempt.builder().id(7).option(option_usa).match(match_tunis).build(),
                                    OptionMatchAttempt.builder().id(8).option(option_usa).match(match_paris).build(),
                                    OptionMatchAttempt.builder().id(9).option(option_usa).match(match_washington).build(),

                                    // the extra one
                                    OptionMatchAttempt.builder().id(10).option(option_usa).match(match_washington).build()
                            )
                    )
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var optionMatchAttempts = questionAttempt.getOptionMatchAttempts();
            assertEquals(0,optionMatchAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(optionMatchAttemptRepository);
        }

        @Test
        void testHandleQuestionAttempt_empty_optionMatchAttempts() {
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
                            List.of(
                                    CorrectOptionMatch.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                    CorrectOptionMatch.builder().id(2).option(option_france).match(match_paris).build(),
                                    CorrectOptionMatch.builder().id(3).option(option_usa).match(match_washington).build()
                            )
                    )
                    .options(List.of(option_tunisia, option_france, option_usa))
                    .matches(List.of(match_tunis, match_paris, match_washington))
                    .build();

            // attempt
            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .optionMatchAttempts(List.of())
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var optionMatchAttempts = questionAttempt.getOptionMatchAttempts();
            assertEquals(0,optionMatchAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(optionMatchAttemptRepository);
        }

        @Test
        void testHandleQuestionAttempt_null_optionMatchAttempts() {
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
                            List.of(
                                    CorrectOptionMatch.builder().id(1).option(option_tunisia).match(match_tunis).build(),
                                    CorrectOptionMatch.builder().id(2).option(option_france).match(match_paris).build(),
                                    CorrectOptionMatch.builder().id(3).option(option_usa).match(match_washington).build()
                            )
                    )
                    .options(List.of(option_tunisia, option_france, option_usa))
                    .matches(List.of(match_tunis, match_paris, match_washington))
                    .build();

            // attempt
            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .optionMatchAttempts(null)
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var optionMatchAttempts = questionAttempt.getOptionMatchAttempts();
            assertEquals(0,optionMatchAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(optionMatchAttemptRepository);
        }

    }

    @Nested
    class Test_OPTION_ORDERING{
        // normal behavior tests
        @Test
        public void testHandleQuestionAttempt__normal() {
            var orderedOption1 = OrderedOption.builder().id(1).option("100").correctPosition(0).build();
            var orderedOption2 = OrderedOption.builder().id(2).option("200").correctPosition(1).build();
            var orderedOption3 = OrderedOption.builder().id(3).option("300").correctPosition(2).build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.OPTION_ORDERING)
                    .question("put the following numbers in ascending order from top to bottom")
                    .orderedOptions(List.of(orderedOption1,orderedOption2,orderedOption3))
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .orderedOptionAttempts(
                            List.of(
                                    OrderedOptionAttempt.builder().id(1).orderedOption(orderedOption1).position(1).build(),
                                    OrderedOptionAttempt.builder().id(2).orderedOption(orderedOption2).position(0).build(),
                                    OrderedOptionAttempt.builder().id(3).orderedOption(orderedOption3).position(2).build()
                            )
                    )
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
            when(orderedOptionAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getOrderedOptionAttempts());

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var orderedOptionAttempts = questionAttempt.getOrderedOptionAttempts();
            assertEquals(1,orderedOptionAttempts.stream().filter(OrderedOptionAttempt::getIsCorrect).count());
            assertEquals(2,orderedOptionAttempts.stream().filter(choiceAttempt -> !choiceAttempt.getIsCorrect()).count());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verify(orderedOptionAttemptRepository).saveAll(any());
        }

        @Test
        void testHandleQuestionAttempt__failure() {
            var orderedOption1 = OrderedOption.builder().id(1).option("100").correctPosition(0).build();
            var orderedOption2 = OrderedOption.builder().id(2).option("200").correctPosition(1).build();
            var orderedOption3 = OrderedOption.builder().id(3).option("300").correctPosition(2).build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.OPTION_ORDERING)
                    .question("put the following numbers in ascending order from top to bottom")
                    .orderedOptions(List.of(orderedOption1,orderedOption2,orderedOption3))
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .orderedOptionAttempts(
                            List.of(
                                    OrderedOptionAttempt.builder().id(1).orderedOption(orderedOption1).position(0).build(),
                                    OrderedOptionAttempt.builder().id(2).orderedOption(orderedOption3).position(1).build(),
                                    OrderedOptionAttempt.builder().id(3).orderedOption(orderedOption2).position(2).build()
                            )
                    )
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
            when(orderedOptionAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getOrderedOptionAttempts());

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var orderedOptionAttempts = questionAttempt.getOrderedOptionAttempts();
            assertEquals(1,orderedOptionAttempts.stream().filter(OrderedOptionAttempt::getIsCorrect).count());
            assertEquals(2,orderedOptionAttempts.stream().filter(choiceAttempt -> !choiceAttempt.getIsCorrect()).count());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verify(orderedOptionAttemptRepository).saveAll(any());
        }

        // unpredicted behavior tests
        @Test
        void testHandleQuestionAttempt__empty_orderedOptionAttempts() {
            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.OPTION_ORDERING)
                    .question("put the following numbers in ascending order from top to bottom")
                    .orderedOptions(
                            List.of(
                                    OrderedOption.builder().id(1).option("100").correctPosition(0).build(),
                                    OrderedOption.builder().id(2).option("200").correctPosition(1).build(),
                                    OrderedOption.builder().id(3).option("300").correctPosition(2).build()
                            )
                    )
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .orderedOptionAttempts(new ArrayList<>())
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var orderedOptionAttempts = questionAttempt.getOrderedOptionAttempts();
            assertEquals(0,orderedOptionAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(orderedOptionAttemptRepository);
        }

        @Test
        void testHandleQuestionAttempt__null_orderedOptionAttempts() {
            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.OPTION_ORDERING)
                    .question("put the following numbers in ascending order from top to bottom")
                    .orderedOptions(
                            List.of(
                                    OrderedOption.builder().id(1).option("100").correctPosition(0).build(),
                                    OrderedOption.builder().id(2).option("200").correctPosition(1).build(),
                                    OrderedOption.builder().id(3).option("300").correctPosition(2).build()
                            )
                    )
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .orderedOptionAttempts(null)
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var orderedOptionAttempts = questionAttempt.getOrderedOptionAttempts();
            assertEquals(0,orderedOptionAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(orderedOptionAttemptRepository);
        }

        @Test
        void testHandleQuestionAttempt__overflow() {
            var orderedOption1 = OrderedOption.builder().id(1).option("100").correctPosition(0).build();
            var orderedOption2 = OrderedOption.builder().id(2).option("200").correctPosition(1).build();
            var orderedOption3 = OrderedOption.builder().id(3).option("300").correctPosition(2).build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.OPTION_ORDERING)
                    .question("put the following numbers in ascending order from top to bottom")
                    .orderedOptions(
                            List.of(
                                    orderedOption1,orderedOption2,orderedOption3
                            )
                    )
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .orderedOptionAttempts(
                            List.of(
                                    OrderedOptionAttempt.builder().id(1).orderedOption(orderedOption1).position(0).build(),
                                    OrderedOptionAttempt.builder().id(2).orderedOption(orderedOption2).position(1).build(),
                                    OrderedOptionAttempt.builder().id(3).orderedOption(orderedOption3).position(2).build(),
                                    OrderedOptionAttempt.builder().id(3).orderedOption(orderedOption3).position(2).build()
                            )
                    )
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var orderedOptionAttempts = questionAttempt.getOrderedOptionAttempts();
            assertEquals(0,orderedOptionAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(orderedOptionAttemptRepository);
    }

        @Test
        void testHandleQuestionAttempt__unrelated_orderedOption() {
            var orderedOption1 = OrderedOption.builder().id(1).option("100").correctPosition(0).build();
            var orderedOption2 = OrderedOption.builder().id(2).option("200").correctPosition(1).build();
            var orderedOption3 = OrderedOption.builder().id(3).option("300").correctPosition(2).build();
            var orderedOption4 = OrderedOption.builder().id(36).option("world war 2").correctPosition(3).build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.OPTION_ORDERING)
                    .question("put the following numbers in ascending order from top to bottom")
                    .orderedOptions(
                            List.of(
                                    orderedOption1,orderedOption2,orderedOption3
                            )
                    )
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .orderedOptionAttempts(
                            List.of(
                                    OrderedOptionAttempt.builder().id(1).orderedOption(orderedOption1).position(0).build(),
                                    OrderedOptionAttempt.builder().id(2).orderedOption(orderedOption2).position(1).build(),
                                    OrderedOptionAttempt.builder().id(3).orderedOption(orderedOption3).position(2).build(),
                                    OrderedOptionAttempt.builder().id(4).orderedOption(orderedOption4).position(3).build()
                            )
                    )
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var orderedOptionAttempts = questionAttempt.getOrderedOptionAttempts();
            assertEquals(0,orderedOptionAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(orderedOptionAttemptRepository);
        }

    }

    @Nested
    class Test_MULTIPLE_CHOICE {
        // normal behavior tests
        @Test
        public void testHandleQuestionAttempt__normal() {
            var choice1 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("7").build();
            var choice2 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("13").build();
            var choice3 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("4").build();
            var choice4 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("26").build();

            Question question = Question.builder()
                .id(1)
                .questionType(QuestionType.MULTIPLE_CHOICE)
                .question("which one of those are prime numbers?")
                .choices(List.of(choice1, choice2, choice3, choice4))
                .build();

            var questionAttempt = QuestionAttempt.builder()
                .id(1)
                .question(question)
                .choiceAttempts(List.of(
                        ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice1).build(),
                        ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice2).build())
                    )
                .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
            when(choiceAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getChoiceAttempts());

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var choices = questionAttempt.getChoiceAttempts();
            assertTrue(choices.get(0).getChoice().getIsCorrect());
            assertTrue(choices.get(1).getChoice().getIsCorrect());
            assertEquals(100F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verify(choiceAttemptRepository).saveAll(any());
        }

        @Test
        public void testHandleQuestionAttempt__part_failure() {
            var choice1 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("7").build();
            var choice2 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("13").build();
            var choice3 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("4").build();
            var choice4 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("26").build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.MULTIPLE_CHOICE)
                    .question("which one of those are prime numbers?")
                    .choices(List.of(choice1, choice2, choice3, choice4))
                    .build();

            var questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .choiceAttempts(List.of(
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice1).build(),
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice3).build())
                    )
                    .build();


            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
            when(choiceAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getChoiceAttempts());

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

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
        public void testHandleQuestionAttempt__full_failure() {
            // Setup
            var choice1 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("7").build();
            var choice2 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("13").build();
            var choice3 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("4").build();
            var choice4 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("26").build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.MULTIPLE_CHOICE)
                    .question("which one of those are prime numbers?")
                    .choices(List.of(choice1, choice2, choice3, choice4))
                    .build();

            var questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .choiceAttempts(List.of(
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice3).build(),
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice4).build())
                    )
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
            when(choiceAttemptRepository.saveAll(any())).thenReturn(questionAttempt.getChoiceAttempts());

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var choices = questionAttempt.getChoiceAttempts();
            assertEquals(0,choices.stream().filter(choiceAttempt -> choiceAttempt.getChoice().getIsCorrect()).count());
            assertEquals(2,choices.stream().filter(choiceAttempt -> !choiceAttempt.getChoice().getIsCorrect()).count());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verify(choiceAttemptRepository).saveAll(any());
        }

        // unpredicted behavior tests
        @Test
        public void testHandleQuestionAttempt__empty_choices() {
            var choice1 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("7").build();
            var choice2 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("13").build();
            var choice3 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("4").build();
            var choice4 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("26").build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.MULTIPLE_CHOICE)
                    .question("which one of those are prime numbers?")
                    .choices(List.of(choice1, choice2, choice3, choice4))
                    .build();

            var questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .choiceAttempts(List.of())
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var choiceAttempts = questionAttempt.getChoiceAttempts();
            assertEquals(0,choiceAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(choiceAttemptRepository);
        }

        @Test
        public void testHandleQuestionAttempt__null_choices() {
            var choice1 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("7").build();
            var choice2 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("13").build();
            var choice3 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("4").build();
            var choice4 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("26").build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.MULTIPLE_CHOICE)
                    .question("which one of those are prime numbers?")
                    .choices(List.of(choice1, choice2, choice3, choice4))
                    .build();

            var questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .choiceAttempts(null)
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var choiceAttempts = questionAttempt.getChoiceAttempts();
            assertEquals(0,choiceAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(choiceAttemptRepository);
        }

        @Test
        public void testHandleQuestionAttempt__overflow_choices() {
            var choice1 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("7").build();
            var choice2 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("13").build();
            var choice3 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("4").build();
            var choice4 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("26").build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.MULTIPLE_CHOICE)
                    .question("which one of those are prime numbers?")
                    .choices(List.of(choice1, choice2, choice3, choice4))
                    .build();

            var questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .choiceAttempts(List.of(
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice1).build(),
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice2).build(),
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice3).build(),
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice4).build(),
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice4).build()
                    ))
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var choiceAttempts = questionAttempt.getChoiceAttempts();
            assertEquals(0,choiceAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(choiceAttemptRepository);
        }

        @Test
        public void testHandleQuestionAttempt__unrelated_choices() {
            // Setup
            var choice1 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("7").build();
            var choice2 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("13").build();
            var choice3 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("4").build();
            var choice4 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("26").build();
            var choice5 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("asser").build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.MULTIPLE_CHOICE)
                    .question("which one of those are prime numbers?")
                    .choices(List.of(choice1, choice2, choice3, choice4))
                    .build();

            var questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .choiceAttempts(List.of(
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice1).build(),
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice5).build()
                    ))
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var choiceAttempts = questionAttempt.getChoiceAttempts();
            assertEquals(0,choiceAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(choiceAttemptRepository);
        }
    }

    @Nested
    class Test_SINGLE_CHOICE{
        // normal behavior tests
        @Test
        public void testHandleQuestionAttempt__normal() {
            var choice1 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("13").build();
            var choice2 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("130").build();
            var choice3 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("40").build();
            var choice4 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("26").build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .question("which one of those is a prime number?")
                    .choices(List.of(choice1, choice2, choice3, choice4))
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .choiceAttempts(List.of(ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice1).build()))
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
            when(choiceAttemptRepository.save(any())).thenReturn(questionAttempt.getChoiceAttempts().get(0));

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var choiceAttempt = questionAttempt.getChoiceAttempts().get(0);
            assertTrue(choiceAttempt.getIsCorrect());
            assertEquals(100F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verify(choiceAttemptRepository).save(any());
        }

        @Test
        public void testHandleQuestionAttempt__failure() {
            var choice1 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("13").build();
            var choice2 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("130").build();
            var choice3 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("40").build();
            var choice4 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("26").build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .question("which one of those is a prime number?")
                    .choices(List.of(choice1, choice2, choice3, choice4))
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .choiceAttempts(List.of(ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice2).build()))
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
            when(choiceAttemptRepository.save(any())).thenReturn(questionAttempt.getChoiceAttempts().get(0));

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var choiceAttempts = questionAttempt.getChoiceAttempts();
            assertEquals(1,choiceAttempts.size());
            assertFalse(choiceAttempts.get(0).getIsCorrect());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verify(choiceAttemptRepository).save(any());
        }

        // unpredicted behavior tests
        @Test
        public void testHandleQuestionAttempt__empty_choices() {
            var choice1 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("13").build();
            var choice2 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("130").build();
            var choice3 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("40").build();
            var choice4 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("26").build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .question("which one of those is a prime number?")
                    .choices(List.of(choice1, choice2, choice3, choice4))
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .choiceAttempts(List.of())
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var choiceAttempts = questionAttempt.getChoiceAttempts();
            assertEquals(0,choiceAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(choiceAttemptRepository);
        }

        @Test
        public void testHandleQuestionAttempt__null_choices() {
            var choice1 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("13").build();
            var choice2 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("130").build();
            var choice3 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("40").build();
            var choice4 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("26").build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .question("which one of those is a prime number?")
                    .choices(List.of(choice1, choice2, choice3, choice4))
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .choiceAttempts(null)
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var choiceAttempts = questionAttempt.getChoiceAttempts();
            assertEquals(0,choiceAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(choiceAttemptRepository);
        }

        @Test
        public void testHandleQuestionAttempt__overflow_choices() {
            var choice1 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("13").build();
            var choice2 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("130").build();
            var choice3 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("40").build();
            var choice4 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("26").build();

            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .question("which one of those is a prime number?")
                    .choices(List.of(choice1, choice2, choice3, choice4))
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .choiceAttempts(List.of(
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice1).build(),
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice2).build()
                    ))
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var choiceAttempts = questionAttempt.getChoiceAttempts();
            assertEquals(0,choiceAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(choiceAttemptRepository);
        }

        @Test
        public void testHandleQuestionAttempt__unrelated_choices() {
            var choice1 = Choice.builder().id(UUID.randomUUID()).isCorrect(true).choice("13").build();
            var choice2 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("130").build();
            var choice3 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("40").build();
            var choice4 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("26").build();
            var choice5 = Choice.builder().id(UUID.randomUUID()).isCorrect(false).choice("asser").build();
            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.SINGLE_CHOICE)
                    .question("which one of those is a prime number?")
                    .choices(List.of(choice1, choice2, choice3, choice4))
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .choiceAttempts(List.of(
                            ChoiceAttempt.builder().id(UUID.randomUUID()).choice(choice5).build()
                    ))
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var choiceAttempts = questionAttempt.getChoiceAttempts();
            assertEquals(0,choiceAttempts.size());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(choiceAttemptRepository);
        }

    }

    @Nested
    class Test_GeneralComparison{
        // normal behavior tests
        @Test
        public void testHandleQuestionAttempt__normal() {
            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.TRUE_FALSE)
                    .question("is 7 a prime number?")
                    .answer(Answer.builder().id(UUID.randomUUID()).answer("true").build())
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .answerAttempt(AnswerAttempt.builder().id(UUID.randomUUID()).answer("true").build())
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
            when(answerAttemptRepository.save(any())).thenReturn(questionAttempt.getAnswerAttempt());

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var answerAttempt = questionAttempt.getAnswerAttempt();
            assertTrue(answerAttempt.getIsCorrect());
            assertEquals(100F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verify(answerAttemptRepository).save(any());
        }

        @Test
        public void testHandleQuestionAttempt__failure() {
            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.TRUE_FALSE)
                    .question("is 7 a prime number?")
                    .answer(Answer.builder().id(UUID.randomUUID()).answer("true").build())
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .answerAttempt(AnswerAttempt.builder().id(UUID.randomUUID()).answer("false").build())
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
            when(answerAttemptRepository.save(any())).thenReturn(questionAttempt.getAnswerAttempt());

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var answerAttempt = questionAttempt.getAnswerAttempt();
            assertFalse(answerAttempt.getIsCorrect());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verify(answerAttemptRepository).save(any());
        }

        // unpredicted behavior tests
        @Test
        public void testHandleQuestionAttempt__null_answer() {
            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.TRUE_FALSE)
                    .question("is 7 a prime number?")
                    .answer(Answer.builder().id(UUID.randomUUID()).answer("true").build())
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .answerAttempt(AnswerAttempt.builder().id(UUID.randomUUID()).answer(null).build())
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);
            when(answerAttemptRepository.save(any())).thenReturn(questionAttempt.getAnswerAttempt());

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            var answerAttempt = questionAttempt.getAnswerAttempt();
            assertFalse(answerAttempt.getIsCorrect());
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verify(answerAttemptRepository).save(any());
        }

        @Test
        public void testHandleQuestionAttempt__null_answerAttempt() {
            Question question = Question.builder()
                    .id(1)
                    .questionType(QuestionType.TRUE_FALSE)
                    .question("is 7 a prime number?")
                    .answer(Answer.builder().id(UUID.randomUUID()).answer("true").build())
                    .build();

            QuestionAttempt questionAttempt = QuestionAttempt.builder()
                    .id(1)
                    .question(question)
                    .answerAttempt(null)
                    .build();

            // Mock behavior
            when(questionAttemptRepository.save(any())).thenReturn(questionAttempt);

            // Call method
            questionLogicService.handleQuestionAttempt(questionAttempt);

            // Assert correctnessPercentage is calculated
            assertEquals(0F, questionAttempt.getCorrectnessPercentage());

            // Verify correctnessPercentage is calculated
            verify(questionAttemptRepository).save(any());
            verifyNoInteractions(answerAttemptRepository);
        }
    }
}
