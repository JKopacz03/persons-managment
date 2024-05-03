package com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions;

public class InvalidWorkDateException extends RuntimeException{
    public InvalidWorkDateException() {
    }

    public InvalidWorkDateException(String message) {
        super(message);
    }
}
