package com.pvl_fon.langchat_backend.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    private String model;
    private String systemPrompt;
}
