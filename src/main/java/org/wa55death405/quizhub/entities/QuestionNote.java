package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/*
    * QuestionNote entity represents a note for a question.
    * Used to store additional information user should know about the question
    * (e.g., a hint, a note about the question, etc.)
*/

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionNote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String note;

    @ManyToOne(optional = false)
    private Question question;
}
