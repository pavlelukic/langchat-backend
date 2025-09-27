package com.pvl_fon.langchat_backend.controller;

import com.pvl_fon.langchat_backend.dto.ChatRequest;
import com.pvl_fon.langchat_backend.dto.ChatResponse;
import com.pvl_fon.langchat_backend.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
@Slf4j
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponse sendMessage(@RequestBody ChatRequest request){
        log.info(("Received message: " + request.getMessage() + " at " + java.time.LocalDateTime.now()));
        log.info("Using model: " + request.getModel());
        try{
            log.info("Calling chat service...");
            String reply = chatService.chat(request);
            log.info("Reply received successfully!");
            return new ChatResponse(reply);
        } catch (Exception ex){
            log.error("Error processing message: " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }

    }

}
