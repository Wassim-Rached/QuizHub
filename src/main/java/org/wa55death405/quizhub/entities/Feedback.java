package org.wa55death405.quizhub.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

/*
    * Feedback entity represents a feedback given by a user.

    @Rules
    * Feedback should have a content
    * Feedback should have a timestamp (will be set automatically)
 */

@Data
@Entity
public class Feedback {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    private Integer rating;
    private String email;
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
}

