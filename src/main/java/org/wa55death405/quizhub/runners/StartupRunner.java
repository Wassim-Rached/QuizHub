package org.wa55death405.quizhub.runners;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.enums.QuizAccessType;
import org.wa55death405.quizhub.interfaces.utils.ISeedDataLoader;
import org.wa55death405.quizhub.repositories.QuizRepository;

import java.util.List;

/*
    * This class is responsible for loading seed data into the database.
    * It reads the quiz_creation.json file from the resources folder and saves the data into the database.
 */

@Component
@Profile({"dev", "prod"})
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner {

    private final QuizRepository quizRepository;
    private final ISeedDataLoader seedDataLoader;

    @Override
    public void run(ApplicationArguments args) {
        List<QuizCreationDTO> quizCreationDTO = seedDataLoader.getQuizCreationDTOs();

        // we want to delete the public quizzes so they don't get duplicated
        var publicQuizzes = quizRepository.findAll().stream().filter(q->q.getQuizAccessType().equals(QuizAccessType.PUBLIC)).toList();

        // remove all public quizzes
        quizRepository.deleteAll(publicQuizzes);

        // save all quizzes
        quizRepository.saveAll(quizCreationDTO.stream().map(q->q.toEntity(null)).toList());
    }
}
