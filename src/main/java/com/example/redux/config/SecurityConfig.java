package com.example.redux.config;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> {
                            try {
                                authz
                                        .and()
                                        .csrf()
                                        .disable()
                                        .authorizeHttpRequests()
                                        .requestMatchers("/api/auth/**")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated();

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder){
        try{
            authenticationManagerBuilder.userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        }catch (Exception e){
            e.getMessage();

        }

    }

    @Bean
    PasswordEncoder  passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
