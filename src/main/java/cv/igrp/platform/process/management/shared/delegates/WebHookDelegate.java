package cv.igrp.platform.process.management.shared.delegates;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component("webHookDelegate")
public class WebHookDelegate implements JavaDelegate {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebHookDelegate.class);

  private final HttpClient httpClient;

  public WebHookDelegate() {
    this.httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(10))
        .build();
  }

  @Override
  public void execute(DelegateExecution execution) {
    LOGGER.info("Entered WebHookDelegate");

    String url = execution.getVariable("webhookUrl", String.class);
    String payload = execution.getVariable("webhookPayload", String.class);

    if (url == null) {
      LOGGER.warn("'webhookUrl' variable is null. Skipping webhook call.");
    }

    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(new URI(url))
          .timeout(Duration.ofSeconds(10))
          .POST(HttpRequest.BodyPublishers.ofString(payload))
          .header("Content-Type", "application/json")
          .build();

      LOGGER.info("Sending POST request to webhook URL: {} with payload: {}", url, payload);

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      LOGGER.info("Webhook call successful. Response code: {}, body: {}", response.statusCode(), response.body());

    } catch (Exception e) {
      LOGGER.error("Error calling webhook: {}", e.getMessage(), e);
      throw new RuntimeException(e);
    }

  }

}
