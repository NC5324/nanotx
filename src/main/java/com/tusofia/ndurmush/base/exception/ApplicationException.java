package com.tusofia.ndurmush.base.exception;

@jakarta.ejb.ApplicationException
public class ApplicationException extends RuntimeException {

    public ApplicationException() {
        super();
    }

    public ApplicationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(final String message) {
        super(message);
    }

    public ApplicationException(final Throwable cause) {
        super(cause);
    }

}
