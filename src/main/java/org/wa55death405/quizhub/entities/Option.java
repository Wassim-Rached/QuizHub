package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/*
    * Option entity represents an option for a question.
    * Used with types (OPTION_MATCHING)

    @Rules
    * Each option should be associated with a Question
    * The options are unique for a question
    * Each option should have a flag to indicate if it is correct

    @Note
    * the Option is the thing on the left (to not confuse with the 'Match')
    * Not in any way related to 'OrderedOption' (which is used with types 'OPTION_ORDERING')
    * The option generally not longs so it stays at 255 characters
* */

@Entity
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"option", "question_id"})
})
public class Option {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String option;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Option comparedOption = (Option) obj;
        if (this.id !=null) return id.equals(comparedOption.id);

        if (this.question != null && comparedOption.question != null
                && this.question.getId() != null && comparedOption.question.getId() != null) {
            return this.question.getId().equals(comparedOption.question.getId()) && this.option.equals(comparedOption.option);
        }
        throw new RuntimeException("Option Can Not Be Compared Without Id or Question Id");

    }
}