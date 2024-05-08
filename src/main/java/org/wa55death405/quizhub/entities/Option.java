package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false)
    private String option;

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