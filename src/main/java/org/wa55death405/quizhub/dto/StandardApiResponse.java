package org.wa55death405.quizhub.dto;

import lombok.Builder;
import lombok.Data;
import org.wa55death405.quizhub.enums.StandardApiStatus;

@Data
public class StandardApiResponse<T>{
    private StandardApiStatus status;
    private String message;
    private T data;

    public StandardApiResponse(StandardApiStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public StandardApiResponse(StandardApiStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public StandardApiResponse(T data) {
        this.status = StandardApiStatus.SUCCESS;
        this.data = data;
    }

}
