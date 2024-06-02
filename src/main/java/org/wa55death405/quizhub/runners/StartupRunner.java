package org.wa55death405.quizhub.runners;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.repositories.QuizRepository;

import java.io.InputStream;
import java.util.List;

/*
    * This class is responsible for loading seed data into the database.
    * It reads the quiz.json file from the resources folder and saves the data into the database.
 */

@Component
@Profile({"dev", "prod"})
@RequiredArgsConstructor
public class StartupRunner implements ApplicationRunner {

    private final QuizRepository quizRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("seed/quiz.json");

        if (inputStream == null) throw new RuntimeException("Failed to load seed data");

        List<QuizCreationDTO> quizCreationDTO = objectMapper.readValue(inputStream, new TypeReference<>(){});

        quizRepository.saveAll(quizCreationDTO.stream().map(q->q.toEntity(null)).toList());

    }
}
