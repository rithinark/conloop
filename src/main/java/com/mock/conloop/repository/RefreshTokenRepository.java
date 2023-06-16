package com.mock.conloop.repository;

public interface RefreshTokenRepository {
    public boolean isPresent(String refreshToken);
    public void save(String refreshToken);
}
