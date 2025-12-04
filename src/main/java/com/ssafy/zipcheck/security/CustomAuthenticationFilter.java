package com.ssafy.zipcheck.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.zipcheck.auth.dto.LoginRequest;
import com.ssafy.zipcheck.auth.dto.LoginResponse;
import com.ssafy.zipcheck.common.response.ApiResponse;
import com.ssafy.zipcheck.jwt.JwtProperties;
import com.ssafy.zipcheck.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    {
        // Handle JSON login requests at the existing endpoint.
        super.setFilterProcessesUrl("/zipcheck/auth/login");
    }

    // 로그인 메소드 ( 컨트롤러 대신에 존재함 )
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                throw new BadCredentialsException("Email or password is missing");
            }
            String email = loginRequest.getEmail();
            String pass = loginRequest.getPassword();
            // log.debug("{} {}", email, pass);
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(email, pass);
            return authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to parse authentication request", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
        String token = jwtTokenProvider.generateAccessToken(principal.getUserId(), principal.getEmail(), principal.isAdmin());
        String tokenType = Optional.ofNullable(jwtProperties.getPrefix()).orElse("Bearer").trim();
        if (tokenType.isEmpty()) {
            tokenType = "Bearer";
        }

        LoginResponse loginResponse = new LoginResponse(token, tokenType);
        ApiResponse<LoginResponse> body = ApiResponse.ok(loginResponse);
        writeResponse(response, HttpStatus.OK, body);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        ApiResponse<Void> body = ApiResponse.<Void>builder()
                .status(HttpStatus.UNAUTHORIZED)
                .message("Invalid email or password")
                .data(null)
                .build();
        writeResponse(response, HttpStatus.UNAUTHORIZED, body);
    }

    private void writeResponse(HttpServletResponse response, HttpStatus status, ApiResponse<?> body) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getWriter(), body);
    }
}
