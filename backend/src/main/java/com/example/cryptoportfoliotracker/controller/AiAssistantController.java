package com.example.cryptoportfoliotracker.controller;

import com.example.cryptoportfoliotracker.service.AiAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AiAssistantController {

    @Autowired
    private AiAssistantService aiAssistantService;

    @PostMapping("/assistant")
    @PreAuthorize("isAuthenticated()") // JWT protection
    public ResponseEntity<String> getAiResponse(@RequestBody String userMessage) {
        String response = aiAssistantService.generateResponse(userMessage);
        return ResponseEntity.ok(response);
    }
}
