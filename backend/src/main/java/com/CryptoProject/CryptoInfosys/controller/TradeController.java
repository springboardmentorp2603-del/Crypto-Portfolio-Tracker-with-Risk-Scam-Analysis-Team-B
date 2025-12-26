package com.CryptoProject.CryptoInfosys.controller;

import com.CryptoProject.CryptoInfosys.dto.TradeRequest;
import com.CryptoProject.CryptoInfosys.mapper.TradeMapper;
import com.CryptoProject.CryptoInfosys.security.JwtUtils;
import com.CryptoProject.CryptoInfosys.service.TradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trades")
@CrossOrigin(
	    origins = "http://localhost:3000",
	    methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS }
	)

public class TradeController {

    private final TradeService tradeService;
    private final JwtUtils jwtUtils;

    public TradeController(TradeService tradeService, JwtUtils jwtUtils) {
        this.tradeService = tradeService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<?> addTrade(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TradeRequest request) {

        try {
            String token = authHeader.substring(7);
            String email = jwtUtils.extractUsername(token);

            return ResponseEntity.ok(tradeService.addTrade(request, email));

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<?> getTrades(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtils.extractUsername(token);

        return ResponseEntity.ok(
                tradeService.getTrades(email)
                        .stream()
                        .map(TradeMapper::toDTO)
                        .toList()
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrade(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtils.extractUsername(token);

        tradeService.deleteTrade(id, email);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTrade(
            @PathVariable Long id,
            @RequestBody TradeRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtils.extractUsername(token);

        return ResponseEntity.ok(tradeService.updateTrade(id, request, email));
    }



}

