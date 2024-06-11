package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import lombok.*;
import org.wa55death405.quizhub.enums.QuestionType;

import java.util.*;

@Entity
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String question;
    @Column(nullable = false)
    private Float coefficient = 1f;
    @Column(nullable = false)
    private QuestionType questionType;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Quiz quiz;

    // for FILL_IN_THE_BLANK
    @Column(columnDefinition = "TEXT")
    private String paragraphToBeFilled;

    // for TRUE_FALSE,SINGLE_CHOICE,SHORT_ANSWER,NUMERIC,FILL_IN_THE_BLANK,
    @OneToMany(mappedBy = "question",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Answer> answers;

    // TODO: Eager fetching might need to be changed to lazy fetching
    // for MULTIPLE_CHOICE, SINGLE_CHOICE
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Choice> choices = new ArrayList<>();

    // for OPTION_ORDERING
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<OrderedOption> orderedOptions = new ArrayList<>();

    // for MATCHING_OPTION
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Match> matches = new ArrayList<>();
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Option> options = new ArrayList<>();
    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<CorrectOptionMatch> correctOptionMatches = new ArrayList<>();

    @Override
    public int hashCode() {
        return Objects.hash(id, question,coefficient);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Question other)) return false;
        return Objects.equals(id, other.id);
    }

    public List<Choice> getCorrectChoices() {
        return choices.stream().filter(Choice::getIsCorrect).toList();
    }

}
