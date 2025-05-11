package com.kenjy.bookapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
    @Value("${cover-folder}")
    private String coverFolderPath;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file-storage/book-covers/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + coverFolderPath);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(HttpMethod.GET, "/api/users/me").hasAnyAuthority(ADMIN, AUTHOR, USER)
                        .requestMatchers(HttpMethod.PUT, "/api/users/update").hasAnyAuthority(ADMIN, AUTHOR, USER)
                        .requestMatchers(HttpMethod.GET, "/api/purchase/check-status").hasAnyAuthority(ADMIN, AUTHOR, USER)
                        .requestMatchers(HttpMethod.POST, "/api/purchase").hasAnyAuthority(ADMIN, AUTHOR, USER)
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasAnyAuthority(ADMIN)
                        .requestMatchers("/api/purchase", "/api/purchase/**").hasAnyAuthority(ADMIN, AUTHOR)
                        .requestMatchers("/api/author", "/api/author/**").hasAnyAuthority(ADMIN, AUTHOR, USER)
                        .requestMatchers("/api/books", "/api/books/**").hasAnyAuthority(ADMIN, AUTHOR, USER)
                        .requestMatchers("/api/users", "/api/users/**").hasAuthority(ADMIN)
                        .requestMatchers("/file-storage/book-covers/**").permitAll()
                        .requestMatchers("/public/**", "/auth/**").permitAll()
                        .requestMatchers("/", "/error", "/csrf", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String AUTHOR = "AUTHOR";
}
