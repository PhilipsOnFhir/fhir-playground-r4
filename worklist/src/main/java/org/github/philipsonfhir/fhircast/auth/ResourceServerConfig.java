package org.github.philipsonfhir.fhircast.auth;

import org.github.philipsonfhir.fhircast.server.Prefix;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    //Here we specify to allow the request to the url /user/getEmployeesList with valid access token and scope read
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                .antMatchers("/api/**")
            .and()
                .authorizeRequests()
//                .antMatchers("/api/fhircast/**").access("#oauth2.hasScope('topic')")
                .antMatchers(Prefix.FHIRCAST_TOPIC+"/**").access("#oauth2.hasScope('topic')")
                .antMatchers(Prefix.FHIRCAST_FHIR+"/*/metadata").permitAll()
                .antMatchers(Prefix.FHIRCAST_FHIR+"/**").access("#oauth2.hasScope('topic')")
                .antMatchers(Prefix.FHIRCAST_WEBSUB+"/**").access("#oauth2.hasScope('fhircast')")
        ;
    }
}
