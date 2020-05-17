package com.zbamba.dev.loancalculatorbackend.service;

import com.zbamba.dev.loancalculatorbackend.model.LoanQuote;
import com.zbamba.dev.loancalculatorbackend.model.LoanQuoteCalculator;
import com.zbamba.dev.loancalculatorbackend.model.exception.InsufficientLendersException;
import com.zbamba.dev.loancalculatorbackend.model.exception.LoanQuoteParameterValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    /**
     * Minimum loan amount in pounds sterling that is allowed to be requested by the user
     */
    final static int MIN_LOAN_AMOUNT = 1000;

    /**
     * Maximum loan amount in pounds sterling that is allowed to be requested by the user
     */
    final static int MAX_LOAN_AMOUNT = 15000;

    /**
     * Loan amount increments that the user is allowed to request in
     */
    final static int LOAN_AMOUNT_INCREMENT = 100;

    @Autowired
    LoanQuoteCalculator quoteCalculator;

    /**
     * Gets the loan quote from the string input.
     * @param amount the string input for the loan amount
     * @return the loan quote
     */
    public ResponseEntity<LoanQuote> getQuote(final String amount) {
        int loanAmount;

        try {
            loanAmount = getLoanAmount(amount);
            LoanQuote loanQuote = quoteCalculator.getQuote(loanAmount);
            return new ResponseEntity<>(loanQuote, HttpStatus.OK);
        } catch (LoanQuoteParameterValidationException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (InsufficientLendersException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets the loan amount from the string input to an integer representation, performing format validation and
     * range validation
     * @param loanAmountAsString a string representing an integer between <code>MIN_LOAN_AMOUNT</code> and
     *                           <code>MAX_LOAN_AMOUNT</code> inclusive and in increments of
     *                           <code>LOAN_AMOUNT_INCREMENT</code>. Must not start with the character <code>0</code> or <code>+</code>.
     * @return an integer representing the loan amount requested
     * @throws LoanQuoteParameterValidationException thrown if the loan amount is not between <code>MIN_LOAN_AMOUNT</code> and
     *                           <code>MAX_LOAN_AMOUNT</code> inclusive or in increments of <code>LOAN_AMOUNT_INCREMENT</code>
     *                           or <code>loanAmountAsString</code> has a leading character of <code>0</code> or <code>+</code>.
     */
    private int getLoanAmount(final String loanAmountAsString) throws LoanQuoteParameterValidationException {
        // validate loan amount
        final int loanAmount;

        try {
            loanAmount = Integer.parseInt(loanAmountAsString);
        } catch (NumberFormatException e) {
            throw new LoanQuoteParameterValidationException("Invalid loan amount format, must be an integer: " + loanAmountAsString, e);
        }

        if (loanAmount < MIN_LOAN_AMOUNT || MAX_LOAN_AMOUNT < loanAmount || loanAmount % LOAN_AMOUNT_INCREMENT != 0) {
            throw new LoanQuoteParameterValidationException("Invalid loan amount, must be any 100 increment between 1000-15000 inclusive: " + loanAmount);
        }

        // parseInt allows leading zero or leading plus, we shouldn't
        final char loanAmountLeadingChar = loanAmountAsString.charAt(0);

        if (loanAmountLeadingChar == '0') {
            throw new LoanQuoteParameterValidationException("Invalid loan amount format, must be an integer without leading zeroes: " + loanAmountAsString);
        } else if (loanAmountLeadingChar == '+') {
            throw new LoanQuoteParameterValidationException("Invalid loan amount format, must be an integer without leading plus: " + loanAmountAsString);
        }

        return loanAmount;
    }
}
