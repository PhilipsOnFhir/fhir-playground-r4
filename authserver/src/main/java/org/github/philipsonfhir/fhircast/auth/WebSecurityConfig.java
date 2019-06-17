package org.github.philipsonfhir.fhircast.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        auth.inMemoryAuthentication()
                .withUser("admin").password(encoder.encode("admin")).authorities("ROLE_USER")
            .and()
                .withUser("practitioner").password(encoder.encode("practitioner")).authorities("ROLE_USER")
            .and()
                .withUser("nurse").password(encoder.encode("nurse")).authorities("ROLE_USER")
            .and()
                .withUser("patient").password(encoder.encode("patient")).authorities("ROLE_USER");

    }

//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        inMemoryUserDetailsManager
//                .
//                .createUser( User.withUsername("admin").password("admin").roles("ADMIN").build());
//
//        return inMemoryUserDetailsManager;
//        return new InMemoryUserDetailsManager(
//                User.withDefaultPasswordEncoder()
//                        .username("enduser")
//                        .password("password")
//                        .roles("USER")
//                        .build());
//    }

//    @Bean
//    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
//        return new MySimpleUrlAuthenticationSuccessHandler();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
            .and()
                .formLogin()
            .and()
                .logout().permitAll()
            .and()
                .csrf().disable();
    }
}