package com.CryptoProject.CryptoInfosys.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AiPromptBuilder {

    public static String build(String portfolioContext, String userQuestion) {
        return """
        You are a professional crypto portfolio analyst.

        RULES:
        - DO NOT give financial advice
        - DO NOT recommend specific buy/sell prices
        - Explain risks clearly
        - Be concise but insightful
        - Use bullet points when helpful

        USER PORTFOLIO:
        %s

        USER QUESTION:
        %s

        RESPONSE FORMAT:
        - Summary
        - Risk Analysis
        - Suggestions
        """.formatted(portfolioContext, userQuestion);
    }
}
