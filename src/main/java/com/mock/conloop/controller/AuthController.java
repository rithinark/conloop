package com.mock.conloop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mock.conloop.model.RefreshTokenRequest;
import com.mock.conloop.model.RegisterRequest;
import com.mock.conloop.model.TokenResponse;
import com.mock.conloop.model.User;
import com.mock.conloop.security.JwtProvider;
import com.mock.conloop.service.RefreshTokenService;
import com.mock.conloop.service.UserService;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private JwtProvider jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping(value = "/login")
    public ResponseEntity<Object> createAuthenticationToken() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtTokenUtil.generateToken(auth);
        String refreshToken = refreshTokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new TokenResponse(refreshToken, token));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Object> saveUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.createUser(registerRequest.toUser()));
    }

    @PostMapping(value = "/refreshtoken")
    public ResponseEntity<Object> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if(refreshTokenService.isValidToken(refreshToken)){
            String jwtToken = jwtTokenUtil.generateToken(jwtTokenUtil.getUsernameFromToken(refreshToken), jwtTokenUtil.getRoles(refreshToken));
            return ResponseEntity.ok(new TokenResponse(request.getRefreshToken(), jwtToken));
        }
        throw new IllegalArgumentException("Invalid Token");
    }

}