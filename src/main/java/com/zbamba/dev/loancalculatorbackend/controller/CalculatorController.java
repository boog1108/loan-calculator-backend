package com.zbamba.dev.loancalculatorbackend.controller;

import com.zbamba.dev.loancalculatorbackend.model.LoanQuote;
import com.zbamba.dev.loancalculatorbackend.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/calculator")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    @RequestMapping(value = "loan_quote", method = GET)
    public ResponseEntity<LoanQuote> getLoanQuote(final @RequestParam(value = "loanAmount") String loanAmount) {
        return calculatorService.getQuote(loanAmount);
    }
}
