package org.wa55death405.quizhub.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
public class Feedback {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(columnDefinition = "TEXT")
    private String content;
    private Integer rating;
    private String email;
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
}

