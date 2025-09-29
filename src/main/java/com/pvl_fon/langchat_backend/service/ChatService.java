package com.pvl_fon.langchat_backend.service;

import com.pvl_fon.langchat_backend.dto.ChatRequest;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ChatService {

    private final Map<String, ChatMemory> memories = new ConcurrentHashMap<>();

    final int DEFAULT_TIMEOUT_SECONDS = 90;

    public String chat(ChatRequest request){
        String apiKey = System.getenv("OPENAI_API_KEY");

        ChatMemory chatMemory = memories.computeIfAbsent(request.getModel(), modelName ->
                TokenWindowChatMemory.withMaxTokens(2000, new OpenAiTokenCountEstimator(modelName))
        );

        List<ChatMessage> currentMessages = chatMemory.messages();

        boolean updateSystemMessage = currentMessages.isEmpty() ||
                !(currentMessages.get(0) instanceof SystemMessage) ||
                !((SystemMessage) currentMessages.get(0)).text().equals(request.getSystemPrompt());

        if(updateSystemMessage) {
            chatMemory.clear();
            chatMemory.add(SystemMessage.from(request.getSystemPrompt()));
        }

        chatMemory.add(UserMessage.from(request.getMessage()));

        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(request.getModel())
                .timeout(Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS))
                .build();

        ChatResponse response = model.chat(chatMemory.messages());

        chatMemory.add(response.aiMessage());

        return response.aiMessage().text();
    }

    public void clearMemory(String modelName){
        if(modelName != null && memories.containsKey(modelName)) {
            memories.remove(modelName);
            log.info("Cleared memory for model: " + modelName);
        }
    }
}
