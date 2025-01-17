package org.wa55death405.quizhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.CorrectOptionMatch;

import java.util.UUID;

public interface CorrectOptionMatchRepository extends JpaRepository<CorrectOptionMatch, UUID>{
}
