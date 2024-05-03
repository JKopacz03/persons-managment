package com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions;

public class NotExistingUserException extends RuntimeException {
    public NotExistingUserException() {
    }

    public NotExistingUserException(String message) {
        super(message);
    }
}
