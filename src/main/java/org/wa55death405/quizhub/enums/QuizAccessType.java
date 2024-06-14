package org.wa55death405.quizhub.enums;

import lombok.Getter;

@Getter
public enum QuizAccessType {
    PUBLIC("PUBLIC"),
    LINKED("LINKED");

    private final String value;


    QuizAccessType(String linked) {
        this.value = linked;
    }
}
