package com.spoticket.gateway.global.util;

import com.spoticket.gateway.global.exception.CustomException;
import com.spoticket.gateway.global.exception.ErrorStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (path.equals("/api/v1/users/login")
                || path.equals("/api/v1/users/logout")
                || path.equals("/api/v1/users/signup")) {
            return chain.filter(exchange);  // 로그인, 로그아웃, 회원가입 경로는 필터를 적용하지 않음
        }

        String token = extractToken(exchange);

        if (token == null || !validateToken(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // JWT에서 사용자 정보 추출
        Claims claims = extractClaims(token);
        if (claims == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 사용자 ID와 역할을 헤더에 추가
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                                 .header("X-User-Id", claims.get("userId").toString())
                                 .header("X-Role", claims.get("role").toString())
                                 .build())
                .build();

        return chain.filter(mutatedExchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        log.error("🚥🚥 [JwtAuthenticationFilter] 유효하지 않은 토큰 🚥🚥");
        throw new CustomException(ErrorStatus.UNAUTHORIZED);
    }

    private boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("🚥🚥 [JwtAuthenticationFilter] 유효하지 않은 토큰 {} 🚥🚥", e.getMessage());
            throw new CustomException(ErrorStatus.UNAUTHORIZED);
        }
    }

    private Claims extractClaims(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            log.error("🚥🚥 [JwtAuthenticationFilter] 만료된 토큰 {} 🚥🚥", e.getMessage());
            throw new CustomException(ErrorStatus.UNAUTHORIZED);
        }
    }

    @Override
    public int getOrder() {
        return -10; // JwtAuthorizationFilter는 JwtAuthenticationFilter 이후에 실행
    }
}
