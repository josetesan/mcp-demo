package es.josetesan.ai.poc.mcp.amadeus.config;

import es.josetesan.ai.poc.mcp.amadeus.service.AIService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    ToolCallbackProvider amadeusTools(AIService aiService) {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(aiService)
                .build();
    }
}
