package com.mock.conloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mock.conloop.model.RegisterRequest;
import com.mock.conloop.security.JwtProvider;
import com.mock.conloop.service.UserService;

@RestController
@RequestMapping("auth")
public class AuthController{

    @Autowired
    private JwtProvider jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<Object> createAuthenticationToken() throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtTokenUtil.generateToken(auth);   
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Object> saveUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.createUser(registerRequest.toUser()));
    }
}