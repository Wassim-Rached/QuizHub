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
    private String option;
    private Integer correctPosition;

    @ManyToOne
    private Question question;

}
