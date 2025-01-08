package com.kiosk.mckiosk.config;

import com.kiosk.mckiosk.model.Order;
import com.kiosk.mckiosk.model.OrderStatus;
import com.kiosk.mckiosk.repository.UserRepository;
import com.kiosk.mckiosk.service.KioskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.kiosk.mckiosk.config.CustomAuthenticationFailureHandler;
import com.kiosk.mckiosk.model.OrderModel;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final OrderModel orderModel;

    public SecurityConfig(UserRepository userRepository, OrderModel orderModel) {
        this.userRepository = userRepository;
        this.orderModel = orderModel;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByLogin(username)
                .map(user -> org.springframework.security.core.userdetails.User.withUsername(user.getLogin())
                        .password(user.getPassword())
                        .authorities("ROLE_" + user.getRole().toUpperCase())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/oauth2/**", "/productlist", "/images/**", "/css/**").permitAll()
                        .requestMatchers("/adminPage").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/ordertype", true)
                        .failureHandler(new CustomAuthenticationFailureHandler())
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/ordertype", true)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/welcome")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
