package com.zbamba.dev.loancalculatorbackend.config;

import com.zbamba.dev.loancalculatorbackend.model.Lender;
import com.zbamba.dev.loancalculatorbackend.model.LoanQuoteCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LoanCalculatorConfig {

    @Autowired
    List<Lender> lenders;

    @Bean("quoteCalculator")
    public LoanQuoteCalculator getLoanQuoteCalculator() {
        return new LoanQuoteCalculator(lenders);
    }
}
