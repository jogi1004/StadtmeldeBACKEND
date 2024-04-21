package com.stadtmeldeapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.stadtmeldeapp.service.JwtAuthFilter;
import com.stadtmeldeapp.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private UserDetailsServiceImpl userService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Cross-Site-Request-Forgery
                .cors(cors -> cors.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/auth/*").permitAll()
                        .requestMatchers("/swagger-ui/*", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/user/*").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/categories/*").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/categories/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categories/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categories/*").hasAuthority("ADMIN")
                        .requestMatchers("/status/*").hasAuthority("ADMIN")
                        .requestMatchers("/reports/*").hasAuthority("ADMIN")
                        .requestMatchers("/css/*").permitAll()
                        .requestMatchers("/js/*").permitAll()
                        .requestMatchers("/images/*").permitAll()
                        .requestMatchers("/static/*").permitAll()
                        .requestMatchers("/templates/*").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/aboutUs").permitAll()
                        .requestMatchers("/contact").permitAll()
                        .requestMatchers("/services").permitAll()
                        .requestMatchers("/reports").hasAuthority("ADMIN")
                        .requestMatchers("/cityInfo").hasAuthority("ADMIN")
                        .requestMatchers("/overview").hasAuthority("ADMIN")
                        .requestMatchers("/reports/admin/*").hasAuthority("ADMIN")
                        .requestMatchers("/reports/*").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/overview")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(
                    logout -> logout
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}