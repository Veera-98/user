package com.org.user1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @SuppressWarnings({ "deprecation", "removal" })
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole("ADMIN") 
            .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole("ADMIN") 
            .requestMatchers(HttpMethod.PATCH, "/api/v1/users/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole("ADMIN")
            .anyRequest().authenticated() 
            .and()
            .httpBasic(); 

        return http.build(); 
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> User.withUsername("admin")
                               .password(passwordEncoder().encode("admin123"))
                               .roles("ADMIN")
                               .build();
    }
}
