package com.TaskManagmentSystem.task.config;

import com.TaskManagmentSystem.task.filters.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class taskConfig {

    //csrf(disabled) not recommended for productionn
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");
        http

                .securityContext(securityContext -> securityContext.requireExplicitSave(false)).
                sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                //NOT RECOMMENDDED FOR PRODUCTION IT IS JUST FOR CAN ACCESS TO IT FROM POSTMAN // WITHOUT CSRF TOKEN
                .csrf(crsf -> crsf.csrfTokenRequestHandler(requestHandler).ignoringRequestMatchers( "/register" , "/addNewTask" ,"/deleteTask/{id}" , "/updateTask/{id}")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .httpBasic(withDefaults())
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterAt(new AuthoritiesLoggingAtFilter(),BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/deleteTask/{id}").hasRole("ADMIN")
                        .requestMatchers("/addNewTask").hasAnyRole("ADMIN" , "USER")
                        .requestMatchers("/updateTask{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/getAll").hasAnyRole("ADMIN" , "USER")
                        .requestMatchers("/getTask/{id}").hasAnyRole("ADMIN" , "USER")
                        .requestMatchers("/user").authenticated()
                        .requestMatchers( "/register").permitAll())
                        .formLogin(withDefaults())
                        .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setMaxAge(3600L);
        configuration.setAllowedMethods(Collections.singletonList("*"));
        // it is * until determine the domain that we will work with it
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**" , configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
