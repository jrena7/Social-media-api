package com.example.socialmedia.service.impl;

import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class InvalidTokenService {

    private final HashSet<String> invalidTokens = new HashSet<>();

    public void addToken(String token) {
        invalidTokens.add(token);
    }
}
