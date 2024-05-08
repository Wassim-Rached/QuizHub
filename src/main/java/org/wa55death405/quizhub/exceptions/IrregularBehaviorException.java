package org.wa55death405.quizhub.exceptions;

/*
    this exception is thrown when
    something irregular happens in the application
    it's normally needs to be addressed if it happens
    it's a better way than just throwing a RuntimeException
 */
public class IrregularBehaviorException extends RuntimeException{
    public IrregularBehaviorException(String message) {
        super(message);
    }
}
