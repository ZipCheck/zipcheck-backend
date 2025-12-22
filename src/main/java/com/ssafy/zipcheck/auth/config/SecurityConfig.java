package com.ssafy.zipcheck.auth.config;

import com.ssafy.zipcheck.auth.jwt.JwtFilter;
import com.ssafy.zipcheck.auth.jwt.JwtProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(
                        Collections.singletonList("http://localhost:3000")
                );
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L);
                configuration.setExposedHeaders(
                        Collections.singletonList("Authorization")
                );

                return configuration;
            }
        }));

        http.csrf(csrf -> csrf.disable());
        http.formLogin(form -> form.disable());
        http.httpBasic(basic -> basic.disable());

        http.authorizeHttpRequests(auth -> auth

                // CORS preflight 허용 (중요)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 프로필 이미지 조회 (공개)
                .requestMatchers(HttpMethod.GET, "/users/*/profile-image")
                .permitAll()

                // 파일 업로드
                .requestMatchers(HttpMethod.POST, "/files/**")
                .hasAnyRole("USER", "ADMIN")

                // 공지사항
                .requestMatchers(HttpMethod.GET, "/notices", "/notices/**")
                .permitAll()

                // 게시글
                .requestMatchers(HttpMethod.GET, "/boards", "/boards/**")
                .permitAll()

                .requestMatchers(HttpMethod.POST, "/boards", "/boards/*/like")
                .hasAnyRole("USER", "ADMIN")

                // 댓글
                .requestMatchers(HttpMethod.GET, "/comments/**")
                .permitAll()

                .requestMatchers(HttpMethod.POST, "/comments")
                .hasAnyRole("USER", "ADMIN")

                // 인증
                .requestMatchers(
                        "/auth/signup",
                        "/auth/login",
                        "/auth/reissue",
                        "/auth/password/reset",
                        "/auth/password/reset-confirm"
                ).permitAll()

                // =========================
                // AI 관련 (테스트 편의를 위해 임시로 permitAll)
                // 배포 전에는 hasRole("ADMIN") 등으로 변경해야 함
                // =========================
                .requestMatchers(HttpMethod.POST, "/api/ai/index-reviews").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/ai/deals/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/ai/apartments/**").permitAll() // 추가된 설정
                // 마이페이지
                .requestMatchers("/users/**")
                .hasAnyRole("USER", "ADMIN")

                .anyRequest().authenticated()
        );


        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
