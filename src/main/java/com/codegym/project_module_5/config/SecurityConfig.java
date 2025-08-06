package com.codegym.project_module_5.config;

import com.codegym.project_module_5.model.Role;
import com.codegym.project_module_5.model.User;
import com.codegym.project_module_5.repository.IRoleRepository;
import com.codegym.project_module_5.repository.IUserRepository;
import com.codegym.project_module_5.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
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

                // Tạo admin test a có thể xóa đi hoặc để lại cho mn test luôn
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@example.com");
                admin.setFullName("Administrator");
                admin.setRole(adminRole);
                userRepo.save(admin);
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
                        .requestMatchers("/login", "/signup", "/api/auth/**", "/forgotPassword").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/restaurants/**").hasAuthority("OWNER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        return http.build();
    }
}
