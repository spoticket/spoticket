package com.spoticket.gateway.global.util;

import com.spoticket.gateway.global.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.spoticket.gateway.global.exception.ErrorStatus.FORBIDDEN;

@Component
@Slf4j
public class AuthorizationFilter implements GlobalFilter, Ordered {

    private final AuthorizationRulesConfig config;

    public AuthorizationFilter(AuthorizationRulesConfig config) {
        this.config = config;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (path.equals("/api/v1/users/login")
                || path.equals("/api/v1/users/logout")
                || path.equals("/api/v1/users/signup")) {
            return chain.filter(exchange);  // 로그인, 로그아웃, 회원가입 경로는 필터를 적용하지 않음
        }

        String method = exchange.getRequest().getMethod().name();

        for (AuthorizationRulesConfig.Rule rule : config.getRules()) {
            if (pathMatches(path, rule.getPath()) && rule.getMethod().contains(method)) {
                if(rule.getRoles().contains("PUBLIC")){
                    return chain.filter(exchange);
                }

                String userRole = exchange.getRequest().getHeaders().getFirst("X-Role");
                if (rule.getRoles().contains(userRole)) {
                    return chain.filter(exchange);
                }
            }
        }

        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        log.error("🚥🚥 [AuthorizationFilter] URL 접근권한 없음 🚥🚥");
        throw new CustomException(FORBIDDEN);
    }

    private boolean pathMatches(String requestPath, String rulePath) {
        return requestPath.startsWith(rulePath.replace("**", ""));
    }

    @Override
    public int getOrder() {
        return -5;
    }
}

