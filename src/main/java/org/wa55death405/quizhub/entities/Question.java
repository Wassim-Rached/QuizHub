package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Check;
import org.wa55death405.quizhub.enums.QuestionType;

import java.util.*;

/*
    * Question entity represents a question for a quiz.
    * Contains most of the relations 💀

    @Rules
    * Each question should be associated with a Quiz
    * Each question should have a coefficient
    * The Coefficient can't be less than 0
    * Each question should have a type,
    * Each question should have a question text
    * Depending on the type, the question should have the necessary fields

    @Note
    * The question text is generally not long, so it stays at 255 characters
 */

// TODO: Eager fetching might need to be changed to lazy fetching

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
    @NotBlank
    private String question;
    @Column(nullable = false)
    @Check(constraints = "coefficient >= 0",name = "question_coefficient_positive_check")
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

    public static final int MIN_FILL_IN_THE_BLANK_BLANKS = 1;
    public static final int MAX_FILL_IN_THE_BLANK_BLANKS = 15;


    public static int countBlanks(String paragraphToBeFilled) {
        return countOccurrences(paragraphToBeFilled, "{{blank}}");
    }

    public static int countBlanksAnswers(String answers) {
        if (answers == null || answers.isBlank() || answers.equals("(|)")) {
            return 0;
        }
        // TODO : this have to be fixed later
        return answers.split("(\\|)").length;
    }

    private static int countOccurrences(String text, String searchString) {
        if (searchString.isEmpty()) {
            return 0;
        }

        int count = 0;
        int fromIndex = 0;

        while ((fromIndex = text.indexOf(searchString, fromIndex)) != -1 ) {
            count++;
            fromIndex += searchString.length();
        }

        return count;
    }
}
