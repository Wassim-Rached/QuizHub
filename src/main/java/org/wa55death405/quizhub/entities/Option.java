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
        Option option = (Option) obj;
        return id.equals(option.id);
    }
}