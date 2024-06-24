package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

/*
    * OrderedOption entity represents an ordered option for a question.
    * Used with types (OPTION_ORDERING)

    @Rules
    * Each ordered option should be associated with a Question
    * The OrderedOption correctOrder number should be unique for a Question
    * The OrderedOption option should be unique for a Question

    @Note
    * Not to be confused with 'Option' entity (which is used with types 'OPTION_MATCHING')
*/

@Entity
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"option", "question_id"}),
        @UniqueConstraint(columnNames = {"correctPosition", "question_id"})
})
public class OrderedOption {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    @NotBlank
    private String option;
    // TODO : correctPosition should be renamed to correctOrder
    @Column(nullable = false)
    private Integer correctPosition;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

}
