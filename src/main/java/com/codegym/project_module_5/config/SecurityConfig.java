package com.codegym.project_module_5.config;

import com.codegym.project_module_5.model.user_model.Role;
import com.codegym.project_module_5.repository.user_repository.IRoleRepository;
import com.codegym.project_module_5.repository.user_repository.IUserRepository;
import com.codegym.project_module_5.service.impl.user_service_impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    @Lazy
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


    @Bean
    CommandLineRunner init(IRoleRepository roleRepo, IUserRepository userRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            if (roleRepo.findByName("ADMIN").isEmpty()) {
                roleRepo.save(new Role(null, "ADMIN"));
            }
            if (roleRepo.findByName("OWNER").isEmpty()) {
                roleRepo.save(new Role(null, "OWNER"));
            }
            if (roleRepo.findByName("CUSTOMER").isEmpty()) {
                roleRepo.save(new Role(null, "CUSTOMER"));
            }

            if (userRepo.findByUsername("admin").isEmpty()) {
                Role adminRole = roleRepo.findByName("ADMIN").orElseThrow(
                        () -> new RuntimeException("Lỗi: Không tìm thấy vai trò ADMIN.")
                );
            }
        };
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/", "/home", "/dish/**", "/restaurant_client/**").permitAll()
                        .requestMatchers("/account/**","/register","/verify-otp/profile/").permitAll()

                        // *** THÊM DÒNG NÀY ĐỂ CHO PHÉP TRUY CẬP GIỎ HÀNG ***
                        .requestMatchers("/cart/**").permitAll()

                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/restaurants/signup").authenticated()
                        .requestMatchers("/restaurants/**").hasAnyAuthority("OWNER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/account/login")
                        .loginProcessingUrl("/account/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/account/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/account/login?logout=true")
                        .permitAll()
                );
        return http.build();
    }
}
