package org.wa55death405.quizhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.QuizAttempt;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Integer> {
}
