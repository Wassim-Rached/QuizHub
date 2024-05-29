package org.wa55death405.quizhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.ChoiceAttempt;

import java.util.UUID;

public interface ChoiceAttemptRepository extends JpaRepository<ChoiceAttempt, UUID>{
}
