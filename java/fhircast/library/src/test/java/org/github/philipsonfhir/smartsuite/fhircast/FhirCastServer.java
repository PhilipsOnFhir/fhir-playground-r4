package org.github.philipsonfhir.smartsuite.fhircast;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.logging.Logger;

@SpringBootApplication
public class FhirCastServer implements ApplicationRunner {
    private static final Logger logger = Logger.getLogger( FhirCastServer.class.getName() );

    public static void main(String[] args) {
        SpringApplication.run(FhirCastServer.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Worklist org.github.philipsonfhir.fhirproxy.controller application started with command-line arguments: {}"+ Arrays.toString(args.getSourceArgs()));
        logger.info("NonOptionArgs: {}"+ args.getNonOptionArgs());
        logger.info("OptionNames: {}"+ args.getOptionNames());
        for (String name : args.getOptionNames()) {
            logger.info("arg-" + name + "=" + args.getOptionValues(name));
        }
    }


}
