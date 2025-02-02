package com.zbamba.dev.loancalculatorbackend.model.exception;

/**
 * Thrown when an argument given to the application is invalid
 */
public class LoanQuoteParameterValidationException extends Exception {
    public LoanQuoteParameterValidationException(final String message) {
        super(message);
    }

    public LoanQuoteParameterValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
