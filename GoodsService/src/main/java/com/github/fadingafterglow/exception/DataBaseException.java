package com.github.fadingafterglow.exception;

public class DataBaseException extends RuntimeException {

    public DataBaseException(String message) {
        super(message);
    }

    public DataBaseException(Throwable cause) {
        super(cause);
    }
}
