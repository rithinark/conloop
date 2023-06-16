package com.mock.conloop.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.mock.conloop.constant.ContextConstant;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    @Value("${refresh.token.validity}")
    public long tokenValidity;

    private final RedisTemplate<String, Object> redisTemplate;

    public RefreshTokenRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isPresent(String refreshToken) {
        String key = buildKey(refreshToken);
        String value = String.valueOf(redisTemplate.opsForValue().get(key));
        return "true".equals(value);
    }

    @Override
    public void save(String refreshToken) {
        String key = buildKey(refreshToken);
        redisTemplate.opsForValue().set(key, "true", tokenValidity * 1000L, TimeUnit.MILLISECONDS);
    }

    private String buildKey(String refreshToken) {
        return String.format("%s:%s", ContextConstant.REFRESH_TOKEN_CACHE, refreshToken);
    }

}
