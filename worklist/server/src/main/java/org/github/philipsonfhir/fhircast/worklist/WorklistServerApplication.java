package org.github.philipsonfhir.fhircast.worklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
public class WorklistServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorklistServerApplication.class, args);
    }
}
