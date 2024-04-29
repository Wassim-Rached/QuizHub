package org.wa55death405.quizhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.AnswerAttempt;

public interface AnswerAttemptRepository extends JpaRepository<AnswerAttempt, Integer> {
}
