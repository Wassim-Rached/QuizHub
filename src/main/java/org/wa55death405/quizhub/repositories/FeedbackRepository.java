package org.wa55death405.quizhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.Feedback;

import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
}
