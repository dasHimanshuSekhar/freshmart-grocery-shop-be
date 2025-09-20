package com.groceryshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()   // 🔑 disable CSRF for APIs
            .cors().and()       // 🔑 enable CORS filter
            .authorizeRequests()
            .anyRequest()
            .permitAll(); // 🚀 allow all requests without auth
    }

    // 🔑 CORS filter bean
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // allow all origins
        config.addAllowedHeader("*");        // allow all headers
        config.addAllowedMethod("*");        // allow all methods (GET, POST, PUT, DELETE, etc.)
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
