package com.pvl_fon.langchat_backend.service;

import com.pvl_fon.langchat_backend.dto.ChatRequest;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    public String chat(ChatRequest request){
        String apiKey = System.getenv("OPENAI_API_KEY");

        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(request.getModel())
                .build();

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(SystemMessage.from(request.getSystemPrompt()));
        messages.add(UserMessage.from(request.getMessage()));

        ChatResponse response = model.chat(messages);

        return response.toString();
    }
}
