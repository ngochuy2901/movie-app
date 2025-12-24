package com.example.demo.configuration;

import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.MyUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthFilter) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable) // disable csrf
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/register", "/user/login", "/video/**", "image/**", "reaction/**", "comment/**", "image/**", "user/get_user_info/*", "user/get_user_info_by_userid/*")
                        .permitAll().requestMatchers("/reaction/on_reaction_click", "comment/save_new_comment")
                        .authenticated()
                        .anyRequest()
                        .authenticated()

                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT không dùng session
                ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username).map(MyUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}