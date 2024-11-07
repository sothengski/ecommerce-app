// package com.group01.ecommerce_app.config;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import
// org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import
// org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
// @Configuration
// @EnableWebSecurity
// public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
// @Bean
// public BCryptPasswordEncoder passwordEncoder() {
// return new BCryptPasswordEncoder();
// }
//
// @Override
// protected void configure(HttpSecurity http) throws Exception {
// http
// .csrf().ignoringAntMatchers("/h2-console/**") // Disable CSRF protection for
// H2 console
// .and()
// .authorizeRequests()
// .antMatchers("/h2-console/**").permitAll() // Allow access to H2 console
// .anyRequest().authenticated() // Require authentication for other requests
// .and()
// .headers().frameOptions().sameOrigin(); // Allow frames from the same origin
// for H2 console
// }
// }