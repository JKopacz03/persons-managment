package com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException() {
    }

    public InvalidCommandException(String message) {
        super(message);
    }
}
