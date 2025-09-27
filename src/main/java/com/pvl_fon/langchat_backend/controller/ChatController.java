package com.pvl_fon.langchat_backend.controller;

import com.pvl_fon.langchat_backend.dto.ChatRequest;
import com.pvl_fon.langchat_backend.dto.ChatResponse;
import com.pvl_fon.langchat_backend.service.ChatService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponse sendMessage(@RequestBody ChatRequest request){
        String reply = chatService.chat(request);
        return new ChatResponse(reply);
    }

}
