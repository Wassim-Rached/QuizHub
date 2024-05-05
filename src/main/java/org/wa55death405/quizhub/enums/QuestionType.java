package org.wa55death405.quizhub.enums;

import lombok.Getter;

@Getter
public enum QuestionType {
    TRUE_FALSE("TRUE_FALSE"),
    SHORT_ANSWER("SHORT_ANSWER"),
    NUMERIC("NUMERIC"),
    FILL_IN_THE_BLANK("FILL_IN_THE_BLANK"),

    SINGLE_CHOICE("SINGLE_CHOICE"),
    MULTIPLE_CHOICE("MULTIPLE_CHOICE"),

    OPTION_MATCHING("OPTION_MATCHING"),

    OPTION_ORDERING("OPTION_ORDERING");

    private final String value;

    QuestionType(String value) {
        this.value = value;
    }
}
