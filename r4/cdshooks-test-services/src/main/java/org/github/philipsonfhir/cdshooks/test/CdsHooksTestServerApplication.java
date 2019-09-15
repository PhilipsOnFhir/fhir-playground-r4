package org.github.philipsonfhir.cdshooks.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
public class CdsHooksTestServerApplication  {

	public static void main(String[] args) {
		SpringApplication.run(CdsHooksTestServerApplication.class, args);
	}
}
