package org.github.philipsonfhir.smartsuite.examples.local;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.util.Arrays;
import java.util.logging.Logger;

@SpringBootApplication
@ServletComponentScan
//@ComponentScan({"my.mainapplication.package","my.library.package"})
public class MyPlainFhirCastServer implements ApplicationRunner {
   private static final Logger logger = Logger.getLogger( MyPlainFhirCastServer.class.getName() );

    public static void main(String[] args) {
        SpringApplication.run(MyPlainFhirCastServer.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("FhirCastExtServer org.github.philipsonfhir.fhirproxy.controller application started with command-line arguments: {}"+ Arrays.toString(args.getSourceArgs()));
        logger.info("NonOptionArgs: {}"+ args.getNonOptionArgs());
        logger.info("OptionNames: {}"+ args.getOptionNames());
        for (String name : args.getOptionNames()) {
            logger.info("arg-" + name + "=" + args.getOptionValues(name));
        }
    }
}
