package com.mock.conloop.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mock.conloop.constant.SecurityConstant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private AuthenticationManager authManager;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Value("${spring.data.rest.login-uri}")
    private String loginUri;

    public JwtFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService,
            AuthenticationManager authManager) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.authManager = authManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        UsernamePasswordAuthenticationToken authentication;
        final Optional<String> token = getTokenFromRequest(request);

        if (token.isEmpty() || !isValidToken(token.get())) {
            filterChain.doFilter(request, response);
            return;
        }
        String authToken = token.get();
        final String uri = request.getRequestURI();

        if (authToken.startsWith(SecurityConstant.BASIC) && uri.endsWith(loginUri)) {
            List<String> usernamePassword = decodedBase64(authToken);
            authentication = new UsernamePasswordAuthenticationToken(usernamePassword.get(0), usernamePassword.get(1));
            Authentication authResult = this.authManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authResult);

        }
        if (authToken.startsWith(SecurityConstant.BEARER) && !uri.endsWith(loginUri)) {
            authToken = StringUtils.delete(authToken, SecurityConstant.BEARER).trim();
            if (!jwtProvider.isTokenExpired(authToken))
                setSecurityContext(new WebAuthenticationDetailsSource().buildDetails(request), authToken);
        }

        filterChain.doFilter(request, response);
    }

    private void setSecurityContext(WebAuthenticationDetails authDetails, String token) {
        final String username = jwtProvider.getUsernameFromToken(token);
        final UserDetails user = userDetailsService.loadUserByUsername(username);
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                null,
                user.getAuthorities());
        authentication.setDetails(authDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static Optional<String> getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstant.AUTHORIZATION);
        if (StringUtils.hasText(token)) {
            return Optional.of(token);
        }
        return Optional.empty();
    }

    private boolean isValidToken(String token) {
        return token.startsWith(SecurityConstant.BEARER) || token.startsWith(SecurityConstant.BASIC);
    }

    public List<String> decodedBase64(String token) {
        String baseToken = StringUtils.delete(token, SecurityConstant.BASIC).trim();
        byte[] decodedBytes = Base64.getDecoder().decode(baseToken);
        return Arrays.asList(new String(decodedBytes).split(":", 2));
    }

}