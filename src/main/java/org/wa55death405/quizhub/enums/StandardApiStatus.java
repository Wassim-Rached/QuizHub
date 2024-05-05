package org.wa55death405.quizhub.enums;

import lombok.Getter;

@Getter
public enum StandardApiStatus {
    SUCCESS("success"),
    FAILURE("failure");

    private final String value;

    StandardApiStatus(String value) {
        this.value = value;
    }
}
