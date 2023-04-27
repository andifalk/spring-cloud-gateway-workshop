package com.example.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {

    @Profile("secure")
    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity httpSecurity) throws Exception {
         httpSecurity
                .authorizeHttpRequests().anyRequest().authenticated()
                .and().oauth2ResourceServer().jwt();
         return httpSecurity.build();
    }

    @Profile("!secure")
    @Bean
    public SecurityFilterChain apiSecurityUnAuthenticated(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests().anyRequest().permitAll()
                .and().oauth2ResourceServer().jwt();
        return httpSecurity.build();
    }
}
