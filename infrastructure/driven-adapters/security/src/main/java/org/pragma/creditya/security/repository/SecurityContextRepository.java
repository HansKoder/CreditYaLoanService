package org.pragma.creditya.security.repository;


import lombok.RequiredArgsConstructor;
import org.pragma.creditya.security.jwt.manager.JwtAuthenticationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtAuthenticationManager jwtAuthenticationManager;

    private Logger logger = LoggerFactory.getLogger(SecurityContextRepository.class);

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String token = exchange.getAttribute("token");
        logger.info("[infra.security] (authenticate) get token {}, create username with token ",token);
        return jwtAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token, token))
                .map(SecurityContextImpl::new);
    }
}