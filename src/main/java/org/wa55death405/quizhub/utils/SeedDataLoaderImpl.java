package org.wa55death405.quizhub.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.dto.quiz.QuizCreationDTO;
import org.wa55death405.quizhub.interfaces.utils.ISeedDataLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeedDataLoaderImpl implements ISeedDataLoader {

    private final ObjectMapper objectMapper;

    @Override
    public List<QuizCreationDTO> getQuizCreationDTOs() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("seed/quiz_creation.json");

        if (inputStream == null) throw new RuntimeException("Failed to load seed data");

        try {
             return objectMapper.readValue(inputStream, new TypeReference<>(){});
        }catch (IOException e) {
            throw new RuntimeException("Failed to load seed data.");
        }

    }
}
