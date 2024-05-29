package org.wa55death405.quizhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.Option;

import java.util.UUID;

public interface OptionRepository extends JpaRepository<Option, UUID> {
}
