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

                    //SI NO HAY USUARIO → solo limpiar headers
                    if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {

                        ServerHttpRequest cleanRequest = exchange.getRequest()
                                .mutate()
                                .headers(headers -> {
                                    headers.remove("X-User-Email");
                                    headers.remove("X-User-Roles");
                                })
                                .build();

                        return chain.filter(exchange.mutate().request(cleanRequest).build());
                    }

                    //EXTRAER DATOS
                    String email = jwt.getSubject();

                    String roles = authentication.getAuthorities()
                            .stream()
                            .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                            .collect(Collectors.joining(","));

                    // 🔥 LIMPIAR + AGREGAR HEADERS
                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                            .mutate()
                            .headers(headers -> {
                                headers.remove("X-User-Email");
                                headers.remove("X-User-Roles");

                                headers.add("X-User-Email", email);
                                headers.add("X-User-Roles", roles);
                            })
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                });
    }
}