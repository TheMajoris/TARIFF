package com.cs203.core.config;

import com.cs203.core.resolver.CustomBearerTokenResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${cors.origin}")
    private List<String> corsOrigins;

    private final UserDetailsService userDetailsService;
    private final CustomBearerTokenResolver customBearerTokenResolver;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, CustomBearerTokenResolver customBearerTokenResolver) {
        this.userDetailsService = userDetailsService;
        this.customBearerTokenResolver = customBearerTokenResolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                // tariff R stuff
                .requestMatchers(HttpMethod.GET, "/api/v1/tariff-rate/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/v1/tariff-rate/calculate").authenticated()

                // tariff CUD stuff
                .requestMatchers(HttpMethod.POST, "/api/v1/tariff-rate/**").hasAuthority("SCOPE_ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/tariff-rate/**").hasAuthority("SCOPE_ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/tariff-rate/**").hasAuthority("SCOPE_ROLE_ADMIN")

                // auth stuff
                .requestMatchers("/api/v1/auth/logout", "/api/v1/auth/refresh").authenticated()

                // everything else
                .anyRequest().permitAll()
        );

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())
                .bearerTokenResolver(customBearerTokenResolver));
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsOrigins);
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
