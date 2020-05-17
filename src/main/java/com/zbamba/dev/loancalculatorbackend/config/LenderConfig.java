package com.zbamba.dev.loancalculatorbackend.config;

import com.opencsv.bean.CsvToBeanBuilder;
import com.zbamba.dev.loancalculatorbackend.model.Lender;
import com.zbamba.dev.loancalculatorbackend.model.exception.LoanQuoteParameterValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Configuration
public class LenderConfig {

    @Value("${lenders.market.file}")
    private String MARKET_FILE_PATH;

    @Autowired
    ResourceLoader resourceLoader;

    @Bean(name = "lenders")
    public List<Lender> getLenders() throws LoanQuoteParameterValidationException, IOException {
        // first argument is market.csv, ensure that it is a file
        final FileReader marketFileReader;

        Resource marketFile = new ClassPathResource(MARKET_FILE_PATH);

        try {
            marketFileReader = new FileReader(marketFile.getFile());
        } catch (FileNotFoundException e) {
            throw new LoanQuoteParameterValidationException("Invalid market file: " + (marketFile.getFile().getPath()));
        }

        // parse the market.csv
        final List<Lender> lenders;

        try {
            //noinspection unchecked
            lenders = new CsvToBeanBuilder(marketFileReader)
                    .withType(Lender.class)
                    .withThrowExceptions(true)
                    .build()
                    .parse();
            System.out.println("Lenders size: " + lenders.size());
            return lenders;
        } catch (RuntimeException e) {
            throw new LoanQuoteParameterValidationException("Unable to parse invalid market file: " + e.getMessage(), e);
        }
    }
}
