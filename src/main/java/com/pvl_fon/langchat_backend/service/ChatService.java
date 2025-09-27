package com.pvl_fon.langchat_backend.service;

import com.pvl_fon.langchat_backend.dto.ChatRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.output.Response;

import java.util.ArrayList;
import java.util.List;

public class ChatService {
    public String chat(ChatRequest request){
        String apiKey = System.getenv("OPENAI_API_KEY");

        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(request.getModel())
                .temperature(0.7)
                .build();

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(SystemMessage.from(request.getSystemPrompt()));
        messages.add(UserMessage.from(request.getMessage()));

        ChatResponse response = model.chat(messages);

        return response.toString();
    }
}
