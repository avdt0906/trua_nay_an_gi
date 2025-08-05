package com.codegym.project_module_5.security;

import com.codegym.project_module_5.model.Role;
import com.codegym.project_module_5.repository.IRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    CommandLineRunner init(IRoleRepository roleRepo) {
        return args -> {
            if (roleRepo.findByName("CUSTOMER").isEmpty()) {
                roleRepo.save(new Role(null, "CUSTOMER"));
            } else if (roleRepo.findByName("ADMIN").isEmpty()) {
                roleRepo.save(new Role(null, "ADMIN"));
            } else if (roleRepo.findByName("OWNER").isEmpty()) {
                roleRepo.save(new Role(null, "OWNER"));
            }
        };
    }
}
