package com.example.cryptoportfoliotracker.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AiAssistantService {

    public String generateResponse(String userMessage) {
        // Gather limited user context (mocked for simplicity)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Assuming email is username
        int holdingsCount = 5; // Mock
        double totalPnL = 1234.56; // Mock

        String context = "User email: " + email + ", Holdings: " + holdingsCount + ", PnL: " + totalPnL;

        // Placeholder AI API call (replace with actual like OpenAI)
        String aiResponse = callPlaceholderAI(userMessage + " Context: " + context);

        return aiResponse;
    }

    private String callPlaceholderAI(String input) {
        // Simulate AI response; in real impl, integrate with external API
        return "AI Response to: " + input;
    }
}
