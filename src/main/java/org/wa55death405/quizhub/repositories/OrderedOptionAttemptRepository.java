package org.wa55death405.quizhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.OrderedOptionAttempt;

import java.util.UUID;

public interface OrderedOptionAttemptRepository extends JpaRepository<OrderedOptionAttempt, UUID>{
}
