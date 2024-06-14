package org.wa55death405.quizhub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.Quiz;
import org.wa55death405.quizhub.enums.QuizAccessType;

import java.util.UUID;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    Page<Quiz> findAllByTitleContainingIgnoreCaseAndQuizAccessTypeEquals(String title, Pageable pageable, QuizAccessType quizAccessType);
}
