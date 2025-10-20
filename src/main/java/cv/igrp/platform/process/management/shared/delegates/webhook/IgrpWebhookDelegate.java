package cv.igrp.platform.process.management.shared.delegates.webhook;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component("igrpWebhookDelegate")
public class IgrpWebhookDelegate implements JavaDelegate {

  private static final Logger log = LoggerFactory.getLogger(IgrpWebhookDelegate.class);

  private final RestClient restClient = RestClient.create();

  public Expression webhookUrl;
  public Expression webhookMethod;

  @Override
  public void execute(DelegateExecution execution) {

    String baseUrl = webhookUrl.getValue(execution).toString();
    String path = (String) execution.getVariable("webhookUrlPath");
    String query = (String) execution.getVariable("webhookUrlQueryParams");

    String url = UriComponentsBuilder.fromUriString(baseUrl)
        .path(path != null ? path : "")
        .query(query != null ? query : "")
        .build()
        .toUriString();

    String method = webhookMethod.getValue(execution).toString().toUpperCase();

    Map<String, Object> payload = (Map<String, Object>) execution.getVariable("webhookPayload");
    Map<String, String> headersMap = (Map<String, String>) execution.getVariable("webhookHeaders");

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      if (headersMap != null) {
        headersMap.forEach(headers::set);
      }

      log.info("[IgrpWebhookDelegate] Sending {} request to {}", method, url);
      log.debug("[IgrpWebhookDelegate] Payload: {}", payload);

      String responseBody = null;
      int statusCode = 0;

      switch (method.toUpperCase()) {
        case "GET" -> {
          var response = restClient.get()
              .uri(url)
              .headers(httpHeaders -> httpHeaders.addAll(headers))
              .retrieve()
              .toEntity(String.class);
          responseBody = response.getBody();
          statusCode = response.getStatusCode().value();
        }
        case "POST" -> {
          var response = restClient.post()
              .uri(url)
              .headers(httpHeaders -> httpHeaders.addAll(headers))
              .body(payload)
              .retrieve()
              .toEntity(String.class);
          responseBody = response.getBody();
          statusCode = response.getStatusCode().value();
        }
        case "PUT" -> {
          var response = restClient.put()
              .uri(url)
              .headers(httpHeaders -> httpHeaders.addAll(headers))
              .body(payload)
              .retrieve()
              .toEntity(String.class);
          responseBody = response.getBody();
          statusCode = response.getStatusCode().value();
        }
        case "DELETE" -> {
          var response = restClient.delete()
              .uri(url)
              .headers(httpHeaders -> httpHeaders.addAll(headers))
              .retrieve()
              .toEntity(String.class);
          responseBody = response.getBody();
          statusCode = response.getStatusCode().value();
        }
        default -> throw new IllegalArgumentException("Unsupported webhookMethod: " + method);
      }

      log.info("[IgrpWebhookDelegate] Response {}: {}", statusCode, responseBody);

    } catch (Exception e) {
      log.error("[IgrpWebhookDelegate] Error calling webhook {}", url, e);
      execution.setVariable("webhookError", e.getMessage());
      throw new RuntimeException("IgrpWebhookDelegate execution failed", e);
    }
  }

}
