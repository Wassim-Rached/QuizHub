package org.wa55death405.quizhub.utils;

import com.github.javafaker.Faker;
import org.wa55death405.quizhub.dto.question.QuestionCreationRequestDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.entities.QuizAttempt;
import org.wa55death405.quizhub.enums.QuestionType;

import java.util.ArrayList;
import java.util.TreeSet;

// TODO: name should be changed to 'FakeDataRandomGenerator'
public class FakeDataGenerator {

    private static final Faker faker = new Faker();

    public static void fill(QuizCreationDTO quizCreationDTO) {
        quizCreationDTO.setTitle(faker.lorem().sentence());
        // Generate one instance for all types of questions
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
                /*
                    * At least 2 choices
                    * At least one choice should be true
                 */
                for (int i = 0; i < faker.random().nextInt(1, 4); i++) {
                    questionCreationRequestDTO.getChoices().put(faker.lorem().word(), faker.bool().bool());
                }
                questionCreationRequestDTO.getChoices().put(faker.lorem().word(), true);
                return;
            case SINGLE_CHOICE:
                /*
                    * At least 2 choices
                    * Only one choice should be true
                 */
                for (int i = 0; i < faker.random().nextInt(2, 4); i++) {
                    questionCreationRequestDTO.getChoices().put(faker.lorem().word(), false);
                }
                questionCreationRequestDTO.getChoices().put(faker.lorem().word(), true);
                return;
            case OPTION_MATCHING:
                /*
                    * At least 2 'Options'
                    * At least 2 'Matches'
                    * Each 'Option' should have at least one match
                    * A match can be assigned to multiple options or none
                 */
                var numberOfOptions = faker.random().nextInt(2, 7);
                var numberOfMatches = faker.random().nextInt(2,7);

                // Generate the matches that will be used
                var matches = new ArrayList<String>();
                for (int i = 0; i < numberOfMatches; i++) {
                    matches.add(faker.lorem().word());
                }

                // Assign random amount of 'Matches' to 'Options'
                for (int i = 0; i < numberOfOptions; i++) {
                    var option = faker.lorem().word();
                    var numberOfMatchesToAssign = faker.random().nextInt(1, matches.size());

                    var randomMatchesToAssign = new TreeSet<String>();
                    for (int j = 0; j < numberOfMatchesToAssign; j++) {
                        randomMatchesToAssign.add(matches.get(faker.random().nextInt(0, numberOfMatches - 1)));
                    }
                    questionCreationRequestDTO.getOptionMatches().put(option, randomMatchesToAssign.stream().toList());
                }
                return;
            case OPTION_ORDERING:
                /*
                    * At least 2 options
                 */
                for (int i = 0; i < faker.random().nextInt(2, 5); i++) {
                    questionCreationRequestDTO.getOrderedOptions().put(i, faker.lorem().word());
                }
                return;
            default:
                throw new IllegalArgumentException("Unknown question type: " + questionType);
        }
    }

    // TODO : looks weird should be changed
    public static Quiz generate_Quiz(){
        QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
        FakeDataGenerator.fill(quizCreationDTO);
        return quizCreationDTO.toEntity(null);
    }

}
