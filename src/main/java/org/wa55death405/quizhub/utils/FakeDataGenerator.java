package org.wa55death405.quizhub.utils;

import com.github.javafaker.Faker;
import org.wa55death405.quizhub.dto.question.QuestionCreationRequestDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.enums.QuestionType;

import java.util.List;

public class FakeDataGenerator {

    private static final Faker faker = new Faker();

    public static void fill(QuizCreationDTO quizCreationDTO) {
        quizCreationDTO.setTitle(faker.lorem().sentence());
        // create all type of questions
        for (QuestionType questionType : QuestionType.values()) {
            QuestionCreationRequestDTO questionCreationRequestDTO = new QuestionCreationRequestDTO();
            FakeDataGenerator.fill(questionCreationRequestDTO,questionType);
            quizCreationDTO.getQuestions().add(questionCreationRequestDTO);
        }
    }

    public static void fill(QuestionCreationRequestDTO questionCreationRequestDTO, QuestionType questionType) {
        questionCreationRequestDTO.setQuestionType(questionType);
        questionCreationRequestDTO.setQuestion(faker.lorem().sentence());
        questionCreationRequestDTO.setCoefficient(1f);
        switch (questionType) {
            case TRUE_FALSE,FILL_IN_THE_BLANK,NUMERIC,SHORT_ANSWER:
                questionCreationRequestDTO.setAnswer(faker.lorem().word());
                return;
            case MULTIPLE_CHOICE:
                for (int i = 0; i < faker.random().nextInt(2, 5); i++) {
                    questionCreationRequestDTO.getChoices().put(faker.lorem().word(), faker.bool().bool());
                }
                return;
            case SINGLE_CHOICE:
                // only one will have true
                for (int i = 0; i < faker.random().nextInt(2, 4); i++) {
                    questionCreationRequestDTO.getChoices().put(faker.lorem().word(), false);
                }
                questionCreationRequestDTO.getChoices().put(faker.lorem().word(), true);
                return;
            case OPTION_MATCHING:
                for (int i = 0; i < faker.random().nextInt(2, 5); i++) {
                    questionCreationRequestDTO.getOptionMatches().put(faker.lorem().word(), List.of(faker.lorem().word()));
                }
                return;
            case OPTION_ORDERING:
                for (int i = 0; i < faker.random().nextInt(2, 5); i++) {
                    questionCreationRequestDTO.getOrderedOptions().put(i, faker.lorem().word());
                }
                return;
            default:
                throw new IllegalArgumentException("Unknown question type: " + questionType);
        }
    }

}
