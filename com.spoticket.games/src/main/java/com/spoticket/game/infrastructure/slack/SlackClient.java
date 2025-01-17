package com.spoticket.game.infrastructure.slack;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.spoticket.game.common.exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlackClient {

  private final MethodsClient client;

  public SlackClient(@Value("${slack.bot-token}") String botToken) {
    this.client = Slack.getInstance().methods(botToken);
  }

  public void sendMessage(String channel, String text) {
    try {
      ChatPostMessageRequest request = ChatPostMessageRequest.builder()
          .channel(channel)
          .text(text)
          .build();

      client.chatPostMessage(request);
    } catch (Exception e) {
      throw new CustomException(BAD_REQUEST.value(), e.getMessage());
    }
  }

}
