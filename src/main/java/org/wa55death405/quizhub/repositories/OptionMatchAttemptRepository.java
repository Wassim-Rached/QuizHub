package org.wa55death405.quizhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.OptionMatchAttempt;

import java.util.UUID;

public interface OptionMatchAttemptRepository extends JpaRepository<OptionMatchAttempt, UUID> {
}
