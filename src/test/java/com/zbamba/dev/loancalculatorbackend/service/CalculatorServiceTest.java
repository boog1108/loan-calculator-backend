package com.zbamba.dev.loancalculatorbackend.service;

import com.zbamba.dev.loancalculatorbackend.model.LoanQuote;
import com.zbamba.dev.loancalculatorbackend.model.LoanQuoteCalculator;
import com.zbamba.dev.loancalculatorbackend.model.exception.InsufficientLendersException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class CalculatorServiceTest {

    @InjectMocks
    private CalculatorService calculatorService;
    @Mock
    private LoanQuoteCalculator loanQuoteCalculator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetQuoteReturnSuccess() throws InsufficientLendersException {
        String amount = "1000";
        LoanQuote loanQuote = new LoanQuote(1000, BigDecimal.valueOf(7), BigDecimal.valueOf(30.88), BigDecimal.valueOf(1111.65));

        when(loanQuoteCalculator.getQuote(anyInt())).thenReturn(loanQuote);

        ResponseEntity<LoanQuote> result = calculatorService.getQuote(amount);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(loanQuote, result.getBody());
    }

    @Test
    public void testGetQuoteParameterValidationException() {
        String amount = "1050";

        ResponseEntity<LoanQuote> result = calculatorService.getQuote(amount);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    public void testGetQuoteInsufficientLendersException() throws InsufficientLendersException {
        String amount = "5000";

        when(loanQuoteCalculator.getQuote(anyInt())).thenThrow(new InsufficientLendersException());
        ResponseEntity<LoanQuote> result = calculatorService.getQuote(amount);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(result.getBody());
    }
}
