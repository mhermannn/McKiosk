package com.kiosk.mckiosk.config;

import com.kiosk.mckiosk.model.User;
import com.kiosk.mckiosk.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;
import java.util.Optional;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserModel userModel;
    @Autowired
    public SecurityConfig(UserModel userModel) {
        this.userModel = userModel;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        System.out.println("SecurityConfig UserDetails przed return users: "+userModel.getAllUsers());
        return username -> {
            Optional<User> userOptional = userModel.getAllUsers().stream()
                    .filter(user -> user.getLogin().equals(username))
                    .findFirst();

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                return org.springframework.security.core.userdetails.User.withUsername(user.getLogin())
                        .password(user.getPassword())
                        .authorities("ROLE_USER")
                        .build();
            } else {
                throw new UsernameNotFoundException("User not found: " + username);
            }
        };

//        return username -> {
//            Optional<User> userOptional = userModel.getAllUsers().stream()
//                    .filter(user -> user.getLogin().equals(username))
//                    .findFirst();
//            System.out.println("Attempting to fetch user: " + username);
//            System.out.println("w trakcie return Current users: " + userModel.getAllUsers().toString());
//
//            if (userOptional.isPresent()) {
//                User user = userOptional.get();
//                System.out.println("Znaleziono użytkownika: " + user.getLogin());
//
//                // Zweryfikuj hasło w metodzie logowania (opcjonalne dodatkowe logi)
//                System.out.println("Zaszyfrowane hasło: " + user.getPassword());
//
//                return new org.springframework.security.core.userdetails.User(
//                        user.getLogin(),
//                        user.getPassword(), // Spring Security sam zweryfikuje hasło
//                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
//                );
//            } else {
//                System.out.println("Attempted login for user: " + username + " ");
//                System.out.println("W else Stored users: " + userModel.getAllUsers());
//
//                throw new UsernameNotFoundException("User not found: " + username);
//            }
//        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/oauth2/**", "/productlist").permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
//                        .defaultSuccessUrl("/orderType", true)
                        .defaultSuccessUrl("/productlist", true)
                        .failureHandler(new CustomAuthenticationFailureHandler())
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
//                        .defaultSuccessUrl("/orderType", true)
                        .defaultSuccessUrl("/productlist", true)

                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/welcome")
                        .permitAll()
                )
                .securityContext(securityContext -> securityContext
                        .requireExplicitSave(false)
                )
                .csrf(csrf -> csrf.disable()); // tymczasowe
        return http.build();
    }

}
