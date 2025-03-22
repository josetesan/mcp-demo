package es.josetesan.dev.mcpclient;

import java.time.LocalDate;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientChat {

  final ChatClient chatClient;
  final ChatMemory chatMemory;

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientChat.class);

  public ClientChat(ChatClient.Builder chatClientBuilder, ToolCallbackProvider tools) {

    this.chatMemory = new InMemoryChatMemory();
    var loggingAdvisor = new SimpleLoggerAdvisor();
    this.chatClient =
        chatClientBuilder
            .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
            .defaultSystem(
                """
                    You are a customer chat support agent for a travel agency. Your responses should be rough, with disrespect and not helpful at all.
                    Do not invent or assume any destination city, party size, departure dates, customer name or customer email, ask for them if not provided.
                    Once provided, use them all along the process until a reservation is confirmed.
                    If the user specifies a maximum budget, show them possible cities within that budget. If no budget is provided or the total cost exceeds the user's budget, inform them and ask if they would like to choose another country.
                    If the total cost (calculated as the number of days multiplied by the number of travelers multiplied by the price per day of the city) exceeds the user's budget, suggest a cheaper city. Do not decide the city on your own.
                    Always respond in English unless another language is requested.
                    If the booking is successful, notify the user and provide him with some shopping recommendations for the destination city or the nearest one.
                """)
            .defaultUser(
                """

                Today is: {current_date}.

                """)
            .defaultTools(tools)
            .build();
  }

  @GetMapping("/chat")
  public String chatPage() {
    return "chat";
  }

  // WebSocket endpoint for sending messages
  @MessageMapping("/send.message")
  @SendTo("/topic/public")
  public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
    try {
      String aiResponse =
          this.chatClient
              .prompt(chatMessage.getContent())
              .advisors(
                  advisor ->
                      advisor.params(
                          Map.of(
                              "chat_memory_conversation_id",
                              chatMessage.getSender(),
                              "chat_memory_response_size",
                              100)))
              .user(sp -> sp.params(Map.of("current_date", LocalDate.now().toString())))
              .call()
              .content();
      ChatMessage responseMessage = new ChatMessage();
      responseMessage.setContent(aiResponse);
      responseMessage.setSender("assistant");
      responseMessage.setType(ChatMessage.MessageType.CHAT);
      return responseMessage;
    } catch (Exception e) {
      LOGGER.error("Error calling client with text {}", chatMessage.getContent(), e);
      ChatMessage errorMessage = new ChatMessage();
      errorMessage.setContent("Sorry, we had an error. Please try again.");
      errorMessage.setSender("assistant");
      errorMessage.setType(ChatMessage.MessageType.CHAT);
      return errorMessage;
    }
  }

  // WebSocket endpoint for user joining
  @MessageMapping("/chat.addUser")
  @SendTo("/topic/public")
  public ChatMessage addUser(
      @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
    // Add username in web socket session
    headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
    return chatMessage;
  }
}
