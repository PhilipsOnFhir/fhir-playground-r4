package com.philips.research.philipsonfhir.fhirproxy.dstu3.applications.fhircast.app;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class FhirCastApplication {

    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication( FhirCastApplication.class );
        sa.setLogStartupInfo(false);
        sa.setBannerMode(Banner.Mode.OFF);
        sa.run(args);
    }
}