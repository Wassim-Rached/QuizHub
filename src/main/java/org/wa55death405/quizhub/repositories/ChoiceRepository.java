package org.wa55death405.quizhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.Choice;

import java.util.UUID;

public interface ChoiceRepository extends JpaRepository<Choice, UUID> {

}
