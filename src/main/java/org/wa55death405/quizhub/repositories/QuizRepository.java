package org.wa55death405.quizhub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.Quiz;

import java.util.UUID;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    Page<Quiz> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);
}
