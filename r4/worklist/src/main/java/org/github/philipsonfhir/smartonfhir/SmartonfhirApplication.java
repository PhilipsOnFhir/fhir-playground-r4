//package org.github.philipsonfhir.smartonfhir;
//
//import javax.servlet.Filter;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import java.util.Arrays;
//
//@SpringBootApplication
//@Order(6)
//public class SmartonfhirApplication
////        extends WebSecurityConfigurerAdapter
//        implements ApplicationRunner {
//    private static final Logger logger = LoggerFactory.getLogger(SmartonfhirApplication.class);
//
//    public static void main(String[] args) {
//        SpringApplication.run(SmartonfhirApplication.class, args);
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        logger.info("Fhirproxy application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));
//        logger.info("NonOptionArgs: {}", args.getNonOptionArgs());
//        logger.info("OptionNames: {}", args.getOptionNames());
//        for (String name : args.getOptionNames()) {
//            logger.info("arg-" + name + "=" + args.getOptionValues(name));
//        }
//    }
//
//    /////////////////////////////////////////
//
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // @formatter:off
//        http.antMatcher("/**")
//                .authorizeRequests()
//                    .antMatchers("/", "/smartonfhir/metadata", "/smartonfhir/.well-known/smart-configuration", "/login**", "/webjars/**", "/error**").permitAll()
//                    .anyRequest().authenticated()
//                ;
//        // @formatter:on
//    }
//
//    @Configuration
//    @EnableResourceServer
//    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            // @formatter:off
//            http.antMatcher("/me").authorizeRequests().anyRequest().authenticated();
//            // @formatter:on
//        }
//    }
//
//}
