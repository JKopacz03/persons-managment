package com.kopacz.JAROSLAW_KOPACZ_TEST_5.exceptions;

public class InvalidCsvException extends RuntimeException {
    public InvalidCsvException() {
    }

    public InvalidCsvException(String message) {
        super(message);
    }

    public InvalidCsvException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCsvException(Throwable cause) {
        super(cause);
    }
}
