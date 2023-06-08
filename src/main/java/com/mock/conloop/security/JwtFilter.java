package com.mock.conloop.security;

import java.io.IOException;
import java.util.Optional;

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

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String BASIC = "Basic ";
    
    private AuthenticationManager authManager;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService, AuthenticationManager authManager) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.authManager = authManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        UsernamePasswordAuthenticationToken authentication;
        final Optional<String> token = getTokenFromRequest(request);

        if(token.isEmpty() || !isValidToken(token.get())){
            filterChain.doFilter(request, response);
            return;
        }
        String authToken = token.get();
        final String uri = request.getRequestURI();

        if(authToken.startsWith(BASIC) && uri.endsWith("/auth/token")){
            authToken = StringUtils.delete(authToken, BASIC).trim();
            String[] usernamePassword = jwtProvider.decodedBase64(authToken);
            authentication = new UsernamePasswordAuthenticationToken(usernamePassword[0], usernamePassword[1]);
            Authentication authResult = this.authManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authResult);

        }
        if (authToken.startsWith(BEARER) && !uri.endsWith("/auth/token")) {
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
        String token = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(token)) {
            return Optional.of(token);
        }
        return Optional.empty();
    }

    private boolean isValidToken(String token){
        return token.endsWith(BEARER) || token.endsWith(BASIC);
    }


}