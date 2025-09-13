package com.Camello.Camello.security;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {
    
    // Set para almacenar tokens invalidados (en producción usar Redis)
    private final Set<String> blacklistedTokens = new HashSet<>();
    
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }
    
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
    
    public void clearBlacklist() {
        blacklistedTokens.clear();
    }
}