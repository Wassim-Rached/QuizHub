package org.wa55death405.quizhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.AnswerAttempt;

import java.util.UUID;

public interface AnswerAttemptRepository extends JpaRepository<AnswerAttempt, UUID> {
}
