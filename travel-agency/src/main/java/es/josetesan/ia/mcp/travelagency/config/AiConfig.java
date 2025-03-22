package es.josetesan.ia.mcp.travelagency.config;

import es.josetesan.ia.mcp.travelagency.service.AiService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

  @Bean
  public ToolCallbackProvider tools(AiService aiService) {
    return MethodToolCallbackProvider.builder().toolObjects(aiService).build();
  }
}
