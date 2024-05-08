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

    @ManyToOne(optional = false)
    private Question question;

}
