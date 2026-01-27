package com.CryptoProject.CryptoInfosys.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public interface AiRateLimitService {
    void validateRequest(String username);
}
