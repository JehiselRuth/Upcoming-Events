package com.Upcoming.Events.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.Upcoming.Events.demo.services.SecurityUserDetailsService;


@ComponentScan 
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    MyBasicAuthenticationEntryPoint authenticationEntryPoint;

    private SecurityUserDetailsService service;

    public WebSecurityConfig(SecurityUserDetailsService service) {
        this.service = service;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     http
      .cors()
      .and()
      .headers(header -> header.frameOptions().sameOrigin())
      .csrf(csrf -> csrf.disable())
      .formLogin(form-> form.disable())
      .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .deleteCookies("JSESSIONID"))   
                        .authorizeRequests((auth) -> auth
                        .antMatchers("/api/register", "/api/login", "/api/events", "/*").permitAll()
                        .antMatchers("/api/logout").hasAnyRole("USER","ADMIN")
                        .antMatchers("/api/events/add").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .userDetailsService(service)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint));
                return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
