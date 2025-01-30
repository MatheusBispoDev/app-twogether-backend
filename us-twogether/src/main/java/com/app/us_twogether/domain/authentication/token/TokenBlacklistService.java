package com.app.us_twogether.domain.authentication.token;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addToBlacklist(String token, long expirationTimeInSeconds) {
        redisTemplate.opsForValue().set(token, "blacklisted", expirationTimeInSeconds, TimeUnit.SECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return Objects.requireNonNull(redisTemplate.hasKey(token));
    }
}
