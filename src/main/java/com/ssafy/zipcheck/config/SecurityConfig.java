package com.ssafy.zipcheck.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (Stateless API)
                .csrf(csrf -> csrf.disable())

                // 세션을 사용하지 않음 (Stateless API)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // HTTP 요청에 대한 접근 권한 설정
                .authorizeHttpRequests(authz -> authz
                        // '/api/deals/**' 경로에 대한 GET 요청은 모두 허용
                        .requestMatchers(HttpMethod.GET, "/zipcheck/map/**").permitAll()
                        // 다른 모든 요청은 인증 필요
                        .anyRequest().authenticated());

        return http.build();
    }
}
