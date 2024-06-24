package org.wa55death405.quizhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.ErrorLog;

import java.util.UUID;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, UUID> {
}
