package org.wa55death405.quizhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wa55death405.quizhub.entities.OrderedOption;

import java.util.UUID;

public interface OrderedOptionRepository extends JpaRepository<OrderedOption, UUID> {
}
