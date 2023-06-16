package com.mock.conloop.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mock.conloop.model.User;
import com.mock.conloop.repository.RefreshTokenRepository;
import com.mock.conloop.security.JwtProvider;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtProvider jwtProvider;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, JwtProvider jwtProvider) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean isValidToken(String refreshToken) {
        return refreshTokenRepository.isPresent(refreshToken) && jwtProvider.isValidToken(refreshToken);
    }

    @Override
    public String generateToken(User user) {
        String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
}
