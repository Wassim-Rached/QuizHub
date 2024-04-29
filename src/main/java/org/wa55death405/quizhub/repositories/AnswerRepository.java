package org.wa55death405.quizhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
