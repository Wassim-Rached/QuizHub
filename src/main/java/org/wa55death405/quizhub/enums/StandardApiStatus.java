package org.wa55death405.quizhub.enums;

import lombok.Getter;

/*
    this enum is used for configuring the status of the api response
    it contains two values currently : success and failure
 */

@Getter
public enum StandardApiStatus {
    SUCCESS("success"),
    FAILURE("failure");

    private final String value;

    StandardApiStatus(String value) {
        this.value = value;
    }
}
