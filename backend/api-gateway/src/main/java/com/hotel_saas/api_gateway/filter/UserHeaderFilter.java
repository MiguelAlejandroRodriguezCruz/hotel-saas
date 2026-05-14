package com.hotel_saas.api_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
public class UserHeaderFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {

                    var authentication = securityContext.getAuthentication();

                    // SIN JWT → dejar pasar normal
                    if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
                        return chain.filter(exchange);
                    }

                    // EXTRAER DATOS
                    String email = jwt.getSubject();

                    String roles = authentication.getAuthorities()
                            .stream()
                            .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                            .collect(Collectors.joining(","));

                    // AGREGAR HEADERS
                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                            .mutate()
                            .headers(headers -> {
                                headers.set("X-User-Email", email);
                                headers.set("X-User-Roles", roles);
                            })
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                })
                // ✅ ESTE ES EL FIX REAL
                .switchIfEmpty(chain.filter(exchange));
    }

}