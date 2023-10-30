package com.example.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {

    @Profile("secure")
    @Bean
    public SecurityFilterChain apiSecurity(HttpSecurity httpSecurity) throws Exception {
         httpSecurity
                 .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                 .oauth2ResourceServer(oauth -> oauth.jwt(withDefaults()));
         return httpSecurity.build();
    }

    @Profile("!secure")
    @Bean
    public SecurityFilterChain apiSecurityUnAuthenticated(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .oauth2ResourceServer(oauth -> oauth.jwt(withDefaults()));
        return httpSecurity.build();
    }
}
