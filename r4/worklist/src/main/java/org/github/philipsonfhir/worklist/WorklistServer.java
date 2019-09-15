package org.github.philipsonfhir.worklist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;

@SpringBootApplication
public class WorklistServer implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(WorklistServer.class);

    public static void main(String[] args) {
        SpringApplication.run(WorklistServer.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Worklist org.github.philipsonfhir.fhirproxy.controller application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));
        logger.info("NonOptionArgs: {}", args.getNonOptionArgs());
        logger.info("OptionNames: {}", args.getOptionNames());
        for (String name : args.getOptionNames()) {
            logger.info("arg-" + name + "=" + args.getOptionValues(name));
        }
    }

}
