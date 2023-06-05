package com.mock.conloop.security;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mock.conloop.model.TokenCache;
import com.mock.conloop.model.User;
import com.mock.conloop.repository.TokenRepository;

@Component
public class JwtProvider implements Serializable {

    @Value("${jwt.token.validity}")
    public long tokenValidity;

    @Value("${jwt.secret.key}")
    public String secretKey;

    @Value("${conloop.issuer.id}")
    public String issuer;

    public static final String ROLES = "ROLES";

    private final TokenRepository tokenRepository;

    public JwtProvider(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, DecodedJWT::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, DecodedJWT::getExpiresAt);
    }

    public List<String> getRoles(String token) {
        return getClaimFromToken(token, claim -> claim.getClaim(ROLES).asList(String.class));
    }

    public <T> T getClaimFromToken(String token, Function<DecodedJWT, T> claimsResolver) {
        DecodedJWT verifier = JWT.require(Algorithm.HMAC256(secretKey))
                .withIssuer(issuer)
                .build().verify(token);
        return claimsResolver.apply(verifier);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Authentication authentication) {
        final User user = (User) authentication.getPrincipal();

        final List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return generateToken(user.getEmail(), roles);
    }

    private String generateToken(String subject, List<String> roles) {
        final long now = System.currentTimeMillis();
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withClaim(ROLES, roles)
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + tokenValidity * 1000L))
                .withJWTId(UUID.randomUUID()
                        .toString())
                .withNotBefore(new Date(now))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public boolean validateToken(String token) {
        final String username = getUsernameFromToken(token);
        return username != null && !isTokenExpired(token);
    }

    public boolean validateTokenWithCache(String token) {
        final String username = getUsernameFromToken(token);
        Optional<TokenCache> tokenCache = tokenRepository.findOne(username);
        if(tokenCache.isPresent())
            return tokenCache.get().getJwtToken().equals(token);
        return false;
    }
}
