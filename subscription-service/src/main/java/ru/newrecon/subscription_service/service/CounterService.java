package ru.newrecon.subscription_service.service;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CounterService {

    private final RedisConnectionFactory connectionFactory;
    private final RedisTemplate<String, Long> redisTemplate;

    public long setCounterValue(String key, int value) {
        RedisAtomicLong counter = new RedisAtomicLong(key, connectionFactory);
        counter.set(value);
        return counter.get();
    }

    public long increment(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, connectionFactory);
        return counter.incrementAndGet();
    }

    public long decrement(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, connectionFactory);
        return counter.decrementAndGet();
    }

    public long getValue(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, connectionFactory);
        return counter.get();
    }

    public boolean deleteCounter(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }
}
