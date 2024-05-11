package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderedOption {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false)
    private String option;
    @Column(nullable = false)
    private Integer correctPosition;

    // TODO : option and question should be unique together
    // TODO : question and correctPosition should be unique together
    @ManyToOne(optional = false)
    private Question question;

}
