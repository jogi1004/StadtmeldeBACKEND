package com.stadtmeldeapp.CustomExceptions;

public class NotAllowedException extends Exception {

    public NotAllowedException() {
        super();
    }

    public NotAllowedException(String message) {
        super(message);
    }

    public NotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAllowedException(Throwable cause) {
        super(cause);
    }
}
