package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

@Configuration // 스프링 설정 클래스 지정, 등록된 Bean 생성 시점
@EnableWebSecurity // 스프링 보안 활성화
public class SecurityConfig { // 스프링에서 보안 관리 클래스

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 인코더 설정
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers
                .addHeaderWriter((request, response) -> {
                    response.setHeader("X-XSS-Protection", "1; mode=block"); // XSS-Protection 헤더 설정
                })
            )
            .csrf(csrf -> csrf.withDefaults()) // CSRF 보호 설정
            .sessionManagement(session -> session
                .invalidSessionUrl("/session-expired") // 세션 만료 시 이동 페이지
                .maximumSessions(1) // 사용자 별 세션 최대 수
                .maxSessionsPreventsLogin(true) // 동시 세션 제한
            )
            .authorizeRequests(authz -> authz
                .anyRequest().authenticated() // 모든 요청에 대해 인증 요구
            );

        return http.build(); // 필터 체인을 통해 보안 설정(HttpSecurity)을 반환
    }
}
