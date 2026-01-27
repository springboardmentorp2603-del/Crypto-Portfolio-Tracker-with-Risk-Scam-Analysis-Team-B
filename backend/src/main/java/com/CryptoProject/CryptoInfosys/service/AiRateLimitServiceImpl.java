package com.CryptoProject.CryptoInfosys.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AiRateLimitServiceImpl implements AiRateLimitService {

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(
                20,
                Refill.greedy(20, Duration.ofMinutes(1))
        );

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    public void validateRequest(String username) {
        Bucket bucket = buckets.computeIfAbsent(username, key -> createNewBucket());

        if (!bucket.tryConsume(1)) {
            throw new RuntimeException("AI rate limit exceeded. Please try again later.");
        }
    }
}
