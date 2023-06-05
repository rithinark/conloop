package com.mock.conloop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mock.conloop.model.AuthRequest;
import com.mock.conloop.model.RegisterRequest;
import com.mock.conloop.model.TokenCache;
import com.mock.conloop.model.User;
import com.mock.conloop.repository.TokenRepository;
import com.mock.conloop.security.JwtProvider;
import com.mock.conloop.service.UserService;

@RestController
public class AuthController{
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenRepository tokenRepository;

    @PostMapping(value = "/auth/token")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest) throws Exception {
        
        final Authentication auth = authenticate(authenticationRequest.getEmail(),
                authenticationRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtTokenUtil.generateToken(auth);   
        User user = (User)auth.getPrincipal();
        tokenRepository.save(user.getEmail(), new TokenCache(token, null));
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Object> saveUser(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.createUser(registerRequest.toUser()));
    }

    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}