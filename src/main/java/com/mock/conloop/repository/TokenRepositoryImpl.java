package com.mock.conloop.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.mock.conloop.constant.ContextConstant;
import com.mock.conloop.model.TokenCache;

@Repository
public class TokenRepositoryImpl implements TokenRepository {

    @Value("${jwt.token.validity}")
    public long tokenValidity;

    private final RedisTemplate<String, Object> redisTemplate;

    public TokenRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<TokenCache> findOne(String userId) {
        String key = buildKey(userId);
        TokenCache tokenCache = (TokenCache) redisTemplate.opsForValue().get(key);
        if(tokenCache == null)
            return Optional.empty();
        return Optional.of(tokenCache);
    }

    @Override
    public void save(String userId, TokenCache tokenCache) {
        String key = buildKey(userId);
        redisTemplate.opsForValue().set(key, tokenCache, tokenValidity * 1000L, TimeUnit.SECONDS);
    }

    private String buildKey(String userId) {
        return String.format("%s:%s", ContextConstant.TOKEN_CACHE_PREFIX, userId);
    }
}