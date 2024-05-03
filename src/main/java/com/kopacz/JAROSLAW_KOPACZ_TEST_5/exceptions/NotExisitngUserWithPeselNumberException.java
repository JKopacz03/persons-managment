package com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions;

public class NotExisitngUserWithPeselNumberException extends RuntimeException {
    public NotExisitngUserWithPeselNumberException() {
    }

    public NotExisitngUserWithPeselNumberException(String message) {
        super(message);
    }
}
