package org.wa55death405.quizhub.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Check;
import org.wa55death405.quizhub.enums.QuestionType;
import org.wa55death405.quizhub.utils.Algorithms;

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
    @Column(nullable = true,columnDefinition = "TEXT",length = 1024)
    private String additionalContext;
    @Column(nullable = true,columnDefinition = "TEXT",length = 1024)
    private String resultExplanation;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Quiz quiz;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<QuestionNote> questionNotes;

    // for FILL_IN_THE_BLANK
    @Column(columnDefinition = "TEXT",length = 1024)
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


    /*
        * Validation Global Rules
    */

    // for coefficient
    public static final int MAX_COEFFICIENT = 10;

    // for question_notes
    public static final int MAX_QUESTION_NOTES = 2;

    // for FILL_IN_THE_BLANK
    public static final int MIN_FILL_IN_THE_BLANK_BLANKS = 1;
    public static final int MAX_FILL_IN_THE_BLANK_BLANKS = 15;

    // for SHORT_ANSWER
    public static final int MAX_SHORT_ANSWER_ANSWERS = 5;
    public static final int MIN_SHORT_ANSWER_ANSWERS = 1;

    // for MULTIPLE_CHOICE, SINGLE_CHOICE
    static public final Integer MAX_NUMBER_OF_CHOICES = 10;
    static public final Integer MIN_NUMBER_OF_CHOICES = 2;

    // for OPTION_ORDERING
    static public final Integer MAX_NUMBER_OF_ORDERED_OPTIONS = 10;
    static public final Integer MIN_NUMBER_OF_ORDERED_OPTIONS = 2;

    // for OPTION_MATCHING
    static public final Integer MAX_NUMBER_OF_OPTION_MATCHES_OPTIONS = 10;
    static public final Integer MAX_NUMBER_OF_OPTION_MATCHES_MATCHES = 10;
    static public final Integer MIN_NUMBER_OF_OPTION_MATCHES_OPTIONS = 2;
    static public final Integer MIN_NUMBER_OF_OPTION_MATCHES_MATCHES = 2;

    /*
        * Helper Methods
     */

    // for MULTIPLE_CHOICE, SINGLE_CHOICE (generally for MULTIPLE_CHOICE)
    public List<Choice> getCorrectChoices() {
        return choices.stream().filter(Choice::getIsCorrect).toList();
    }


    // for FILL_IN_THE_BLANK
    public static int countBlanks(String paragraphToBeFilled) {
        if (paragraphToBeFilled == null || paragraphToBeFilled.isBlank()) {
            return 0;
        }
        return Algorithms.countStringOccurrences(paragraphToBeFilled,"{{blank}}");
    }
    public static int countBlanksAnswers(String answers) {
        if ( answers == null || answers.isBlank() ) {
            return 0;
        }

        return Algorithms.countWordsSeparatedByDelimiter(answers,"(|)");
    }


    /*
        Object Methods
    */

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

}
