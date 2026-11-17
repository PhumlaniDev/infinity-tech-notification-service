package com.phumlanidev.notificationservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/actuator/health/**", "/actuator/info", "/actuator/prometheus").permitAll()
                    .requestMatchers("/api/v1/notifications/email-verification", "/api/v1/notifications/password-reset").permitAll()
                    .anyRequest().denyAll())
            .oauth2ResourceServer(
                    oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .sessionManagement(
                    s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}