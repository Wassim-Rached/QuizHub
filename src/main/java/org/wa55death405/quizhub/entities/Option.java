package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String option;

    // TODO : option and question should be unique together
    @ManyToOne(optional = false)
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