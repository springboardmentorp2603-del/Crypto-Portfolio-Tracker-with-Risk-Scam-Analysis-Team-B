package com.CryptoProject.CryptoInfosys.controller;

import com.CryptoProject.CryptoInfosys.dto.AiChatRequest;
import com.CryptoProject.CryptoInfosys.dto.AiChatResponse;
import com.CryptoProject.CryptoInfosys.service.AiAssistantService;
import com.CryptoProject.CryptoInfosys.service.AiRateLimitService;
import com.CryptoProject.CryptoInfosys.service.PortfolioContextService;
import com.CryptoProject.CryptoInfosys.service.AiPromptBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:3000")
public class AiAssistantController {

    private final AiAssistantService aiAssistantService;
    private final PortfolioContextService portfolioContextService;
    private final AiRateLimitService aiRateLimitService;

   
    public AiAssistantController(
            AiAssistantService aiAssistantService,
            PortfolioContextService portfolioContextService,
            AiRateLimitService aiRateLimitService
    ) {
        this.aiAssistantService = aiAssistantService;
        this.portfolioContextService = portfolioContextService;
        this.aiRateLimitService = aiRateLimitService;
    }

    
    @PostMapping("/chat")
    public AiChatResponse chat(
            @RequestBody AiChatRequest request,
            Authentication authentication
    ) {
        if (authentication == null) {
            throw new RuntimeException("Unauthorized");
        }

        String userEmail = authentication.getName();

        aiRateLimitService.validateRequest(userEmail);

        String portfolioContext =
                portfolioContextService.buildContextForUser(userEmail);

        String prompt = AiPromptBuilder.build(
                portfolioContext,
                request.getMessage()
        );

        String reply = aiAssistantService.getResponse(prompt, userEmail);

        return new AiChatResponse(reply);
    }
}
