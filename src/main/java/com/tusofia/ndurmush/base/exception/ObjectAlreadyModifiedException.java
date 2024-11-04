package com.tusofia.ndurmush.base.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ObjectAlreadyModifiedException extends RuntimeException {

    private transient Object currentObject;

    public ObjectAlreadyModifiedException() {
        super("Object is already modified by a different user");
    }

    public ObjectAlreadyModifiedException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ObjectAlreadyModifiedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ObjectAlreadyModifiedException(final String message) {
        super(message);
    }

    public ObjectAlreadyModifiedException(final Throwable cause) {
        super(cause);
    }

    public Object getCurrentObject() {
        return currentObject;
    }

    public void setCurrentObject(Object currentObject) {
        this.currentObject = currentObject;
    }

}
