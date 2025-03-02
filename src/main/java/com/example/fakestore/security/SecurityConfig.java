package com.example.fakestore.security;

import com.example.fakestore.security.jwt.JwtAuthenticationEntryPoint;
import com.example.fakestore.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;

    private static final String[] AUTHEN_ENDPOINT = {
            "/api/v1/auth/**", "api/v1/file/upload", "/api/v1/forgotPassword/**"
    };

    private static final String[] ITEM_ENDPOINT = {
            "/api/v1/products/**", "/api/v1/categories/**"
    };

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    // old version - author: H.ANH
    // @Bean
    // SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http.csrf(csrf -> csrf.disable())
    //             .authorizeHttpRequests(authorize -> authorize
    //                     .requestMatchers(AUTHEN_ENDPOINT)
    //                     .permitAll()
    //                     .requestMatchers(HttpMethod.GET, ITEM_ENDPOINT)
    //                     .permitAll()
    //                     .anyRequest()
    //                     .authenticated())
    //             .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
    //             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    //     http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

    //     return http.build();
    // }


    // new version - author: Hien
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTHEN_ENDPOINT).permitAll()
                        .requestMatchers(HttpMethod.GET, ITEM_ENDPOINT).permitAll()
                        .requestMatchers("/api/v1/stripe/checkout").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    
}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Allow Angular frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Allow authentication headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}