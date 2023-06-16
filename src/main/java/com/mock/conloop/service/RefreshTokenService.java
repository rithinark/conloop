package com.mock.conloop.service;

import com.mock.conloop.model.User;

public interface RefreshTokenService {
    public boolean isValidToken(String refreshToken);
    public String generateToken(User user);
}
