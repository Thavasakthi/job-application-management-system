package com.jobapp.exception;

public class JobAppException extends Exception {
    public JobAppException(String message) {
        super(message);
    }

    public JobAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
