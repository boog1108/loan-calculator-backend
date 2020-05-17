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
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
        final InputStreamReader marketFileReader;

        InputStream marketFile = new ClassPathResource(MARKET_FILE_PATH).getInputStream();

        marketFileReader = new InputStreamReader(marketFile, StandardCharsets.UTF_8);

        // parse the market.csv
        final List<Lender> lenders;

        try {
            //noinspection unchecked
            lenders = new CsvToBeanBuilder(marketFileReader)
                    .withType(Lender.class)
                    .withThrowExceptions(true)
                    .build()
                    .parse();
            System.out.println("lenders size: " + lenders.size());
            return lenders;
        } catch (RuntimeException e) {
            throw new LoanQuoteParameterValidationException("Unable to parse invalid market file: " + e.getMessage(), e);
        }
    }
}
