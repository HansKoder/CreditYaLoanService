package org.pragma.creditya.security.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.pragma.creditya.model.loan.gateways.UserInfoRepository;
import org.pragma.creditya.security.exceptions.TokenInvalidInfraException;
import org.pragma.creditya.security.jwt.provider.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoRepository {

    private Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    private final JwtProvider jwtProvider;

    @Override
    public Mono<String> getUsername() {

        return Mono.deferContextual(ctx -> {
            String token = Optional.of(ctx.get("token"))
                    .map(Object::toString)
                    .orElseThrow(() -> new TokenInvalidInfraException("Missing Token"));

            logger.info("[infra.security] (getUsername) ");
            Claims claims = jwtProvider.getClaims(token);
            return Mono.justOrEmpty(claims.getSubject());
        });


    }
}
