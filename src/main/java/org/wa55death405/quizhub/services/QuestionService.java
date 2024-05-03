package org.wa55death405.quizhub.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.wa55death405.quizhub.dto.QuestionTakingDTO;
import org.wa55death405.quizhub.repositories.QuestionRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ModelMapper modelMapper;

    public List<QuestionTakingDTO> getQuestionsForTakingQuiz(Integer quizId) {
        return questionRepository.findByQuizId(quizId).stream()
                .map(question -> modelMapper.map(question, QuestionTakingDTO.class))
                .toList();
    }

}
