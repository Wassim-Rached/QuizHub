package org.wa55death405.quizhub.utils;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.dto.question.QuestionCreationRequestDTO;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.entities.Question;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.enums.QuestionType;
import org.wa55death405.quizhub.enums.QuizAccessType;
import org.wa55death405.quizhub.interfaces.utils.IFakeDataRandomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/*
    * This class is responsible for generating fake data
    not to be mistaken with 'FakeDataLogicalGeneratorImpl'
    * this class is used to generate fake data
    that requires random generations
 */
@Service
@RequiredArgsConstructor
public class FakeDataRandomGeneratorImpl implements IFakeDataRandomGenerator {

    private final Faker faker;

    @Override
    public void fill(QuizCreationDTO quizCreationDTO) {
        quizCreationDTO.setTitle(faker.lorem().sentence());
        quizCreationDTO.setQuizAccessType(QuizAccessType.PUBLIC);
        // Generate one instance for all types of questions
        for (QuestionType questionType : QuestionType.values()) {
            QuestionCreationRequestDTO questionCreationRequestDTO = new QuestionCreationRequestDTO();
            this.fill(questionCreationRequestDTO,questionType);
            quizCreationDTO.getQuestions().add(questionCreationRequestDTO);
        }
    }

    // TODO: separate it to multiple methods
    @Override
    public void fill(QuestionCreationRequestDTO questionCreationRequestDTO, QuestionType questionType) {
        questionCreationRequestDTO.setQuestionType(questionType);
        questionCreationRequestDTO.setQuestion(faker.lorem().sentence());
        questionCreationRequestDTO.setCoefficient(1f);
        switch (questionType) {
            case TRUE_FALSE,NUMERIC,SHORT_ANSWER:
                questionCreationRequestDTO.setAnswers(new ArrayList<>(List.of(faker.lorem().word())));
                return;

            case FILL_IN_THE_BLANK:
                List<String> answers = new ArrayList<>();
                // persist the numberOfBlanks as variable,
                // so we can set the same number as answers
                int numberOfBlanks = faker.random().nextInt(Question.MIN_FILL_IN_THE_BLANK_BLANKS, Question.MAX_FILL_IN_THE_BLANK_BLANKS);
                for (int i = 0; i < numberOfBlanks; i++) {
                    answers.add(faker.lorem().word());
                }
                questionCreationRequestDTO.setAnswers(new ArrayList<>(List.of(String.join("(|)",answers))));

                // this just needs to not be null the backend logic wouldn't care about the value
                // as long as it includes at least one blank separator currently "{{blank}}"
                // it is used by the frontend to display the paragraph to help the user answer
                StringBuilder paragraphToBeFilled = new StringBuilder();
                for (int i = 0; i < numberOfBlanks; i++) {
                    paragraphToBeFilled.append(faker.lorem().sentence()).append(" {{blank}} ");
                }
                questionCreationRequestDTO.setParagraphToBeFilled(paragraphToBeFilled.toString());
                return;

            case MULTIPLE_CHOICE:
                /*
                    * Random number of choices between the MIN and MAX
                    * At least one choice should be true
                 */
                for (int i = 0; i < faker.random().nextInt(Question.MIN_NUMBER_OF_CHOICES, Question.MAX_NUMBER_OF_CHOICES - 1); i++) {
                    questionCreationRequestDTO.getChoices().put(faker.lorem().word(), faker.bool().bool());
                }
                questionCreationRequestDTO.getChoices().put(faker.lorem().word(), true);
                return;
            case SINGLE_CHOICE:
                /*
                    * Random amount of choices between the MIN and MAX
                    * Only one choice should be true
                 */
                for (int i = 0; i < faker.random().nextInt(Question.MIN_NUMBER_OF_CHOICES, Question.MAX_NUMBER_OF_CHOICES - 1); i++) {
                    questionCreationRequestDTO.getChoices().put(faker.lorem().word(), false);
                }
                questionCreationRequestDTO.getChoices().put(faker.lorem().word(), true);
                return;

            case OPTION_MATCHING:
                /*
                    * Random amount of 'Options' between the MIN and MAX
                    * Random amount of 'Matches' between the MIN and MAX
                    * Each 'Option' should have at least one match
                    * A match can be linked to multiple options or none
                 */
                var numberOfOptions = faker.random().nextInt(Question.MIN_NUMBER_OF_OPTION_MATCHES_OPTIONS, Question.MAX_NUMBER_OF_OPTION_MATCHES_OPTIONS);
                var numberOfMatches = faker.random().nextInt(Question.MIN_NUMBER_OF_OPTION_MATCHES_MATCHES, Question.MAX_NUMBER_OF_OPTION_MATCHES_MATCHES);

                // Generate the matches that will be used
                var matches = new ArrayList<String>();
                for (int i = 0; i < numberOfMatches; i++) {
                    matches.add(i + faker.lorem().word());
                }

                // Assign random amount of 'Matches' to 'Options'
                for (int i = 0; i < numberOfOptions; i++) {
                    var option = faker.lorem().word()+i;
                    var numberOfMatchesToAssign = faker.random().nextInt(Question.MIN_NUMBER_OF_OPTION_MATCHES_MATCHES, matches.size());

                    var randomMatchesToAssign = new TreeSet<String>();
                    for (int j = 0; j < numberOfMatchesToAssign; j++) {
                        randomMatchesToAssign.add(matches.get(faker.random().nextInt(0, numberOfMatches - 1)));
                    }
                    questionCreationRequestDTO.getOptionMatches().put(option, randomMatchesToAssign.stream().toList());
                }
                return;
            case OPTION_ORDERING:
                /*
                    * Random number of ordered options between the MIN and MAX
                 */
                for (int i = 0; i < faker.random().nextInt(Question.MIN_NUMBER_OF_ORDERED_OPTIONS, Question.MAX_NUMBER_OF_ORDERED_OPTIONS); i++) {
                    questionCreationRequestDTO.getOrderedOptions().put(i, faker.lorem().word()+i);
                }
                return;

            default:
                throw new IllegalArgumentException("Unknown question type: " + questionType);
        }
    }

    // TODO : looks weird should be changed
    @Override
    public Quiz generate_Quiz(){
        QuizCreationDTO quizCreationDTO = new QuizCreationDTO();
        this.fill(quizCreationDTO);
        return quizCreationDTO.toEntity(null);
    }

}
