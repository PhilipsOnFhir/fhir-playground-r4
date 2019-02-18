package com.github.philipsonfhir.fhircast;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class FhirCastHubServerApplication {
    org.springframework.web.servlet.HandlerMapping a;
    javax.servlet.annotation.WebServlet b;
    org.apache.commons.logging.LogFactory c;

    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication( FhirCastHubServerApplication.class );
        sa.setLogStartupInfo(false);
        sa.setBannerMode( Banner.Mode.OFF);
        sa.run(args);
    }
}
