package com.sustainabilitytracker.sustainabilitytracker.config;

import com.sustainabilitytracker.sustainabilitytracker.enums.Role;
import com.sustainabilitytracker.sustainabilitytracker.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.name())
                        .requestMatchers("/departments/**").hasRole(Role.ADMIN.name())
                        .requestMatchers("/companies/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/emissions").hasAnyRole(
                                Role.EMPLOYEE.name(),
                                Role.DEPT_MANAGER.name(),
                                Role.SUSTAINABILITY_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/emissions/*/submit").hasAnyRole(
                                Role.EMPLOYEE.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/emissions/*/aprove").hasAnyRole(
                                Role.SUSTAINABILITY_MANAGER.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/emissions/*/reject").hasAnyRole(
                                Role.SUSTAINABILITY_MANAGER.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        // energy
                        .requestMatchers(HttpMethod.POST, "/energies").hasAnyRole(
                                Role.EMPLOYEE.name(),
                                Role.DEPT_MANAGER.name(),
                                Role.SUSTAINABILITY_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/energies/*/submit").hasAnyRole(
                                Role.EMPLOYEE.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/energies/*/aprove").hasAnyRole(
                                Role.SUSTAINABILITY_MANAGER.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/energies/*/reject").hasAnyRole(
                                Role.SUSTAINABILITY_MANAGER.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        // waste
                        .requestMatchers(HttpMethod.POST, "/waste").hasAnyRole(
                                Role.EMPLOYEE.name(),
                                Role.DEPT_MANAGER.name(),
                                Role.SUSTAINABILITY_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/waste/*/submit").hasAnyRole(
                                Role.EMPLOYEE.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/waste/*/aprove").hasAnyRole(
                                Role.SUSTAINABILITY_MANAGER.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/waste/*/reject").hasAnyRole(
                                Role.SUSTAINABILITY_MANAGER.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        // Social
                        .requestMatchers(HttpMethod.POST, "/social").hasAnyRole(
                                Role.EMPLOYEE.name(),
                                Role.DEPT_MANAGER.name(),
                                Role.SUSTAINABILITY_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/social/*/submit").hasAnyRole(
                                Role.EMPLOYEE.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/social/*/aprove").hasAnyRole(
                                Role.SUSTAINABILITY_MANAGER.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/social/*/reject").hasAnyRole(
                                Role.SUSTAINABILITY_MANAGER.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        // Governance
                        .requestMatchers(HttpMethod.POST, "/governance").hasAnyRole(
                                Role.EMPLOYEE.name(),
                                Role.DEPT_MANAGER.name(),
                                Role.SUSTAINABILITY_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/governance/*/submit").hasAnyRole(
                                Role.EMPLOYEE.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/governance/*/aprove").hasAnyRole(
                                Role.SUSTAINABILITY_MANAGER.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        .requestMatchers(HttpMethod.PUT, "/governance/*/reject").hasAnyRole(
                                Role.SUSTAINABILITY_MANAGER.name(),
                                Role.DEPT_MANAGER.name()
                        )
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint((req, res, ex1) -> res.setStatus(HttpStatus.UNAUTHORIZED.value()));
                    ex.accessDeniedHandler((req, res, ex1) -> res.setStatus(HttpStatus.FORBIDDEN.value()));
                });

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}