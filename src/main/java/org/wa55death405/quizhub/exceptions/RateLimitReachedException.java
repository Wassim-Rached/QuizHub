package org.wa55death405.quizhub.exceptions;

public class RateLimitReachedException extends RuntimeException{
    public RateLimitReachedException(String message) {
        super(message);
    }
}
