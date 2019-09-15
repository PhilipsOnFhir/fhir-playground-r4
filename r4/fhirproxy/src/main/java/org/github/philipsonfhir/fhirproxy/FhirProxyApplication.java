package org.github.philipsonfhir.fhirproxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ServletComponentScan
public class FhirProxyApplication
//        extends WebSecurityConfigurerAdapter
        implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(FhirProxyApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FhirProxyApplication.class, args);
    }

    public void run(ApplicationArguments args) throws Exception {
        logger.info(String.format("Fhirproxy application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs())));
        logger.info(String.format("NonOptionArgs: {}", args.getNonOptionArgs()));
        logger.info(String.format("OptionNames: {}", args.getOptionNames()));
        for (String name : args.getOptionNames()) {
            logger.info("arg-" + name + "=" + args.getOptionValues(name));
        }
    }

}
