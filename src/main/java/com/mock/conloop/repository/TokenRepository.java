package com.mock.conloop.repository;

import java.util.Optional;

import com.mock.conloop.model.TokenCache;

public interface TokenRepository {
    public Optional<TokenCache> findOne(String key);
    public void save(String key, TokenCache tokenCache);
}