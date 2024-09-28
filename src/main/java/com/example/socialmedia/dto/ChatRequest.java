package com.example.socialmedia.dto;

import java.util.ArrayList;

public record ChatRequest(
        ArrayList<String> receivers
) {
}