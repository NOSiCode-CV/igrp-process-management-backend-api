package cv.igrp.platform.process.management.shared.delegates.webhook;

import cv.igrp.platform.process.management.shared.util.ObjectUtil;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Objects;

import static java.util.Optional.ofNullable;

@Component("igrpWebhookDelegate")
public class IgrpWebhookDelegate implements JavaDelegate {

  private static final Logger log = LoggerFactory.getLogger(IgrpWebhookDelegate.class);

  private final RestClient restClient = RestClient.create();

  @Value(value = "${igrp.delegate.webhook.auth-token:}")
  private String globalAuthToken;

  public Expression webhookUrl;
  public Expression webhookMethod;
  public Expression webhookUrlPath;
  public Expression webhookQueryParams;
  public Expression webhookPayload;
  public Expression webhookHeaders;

  @Override
  public void execute(DelegateExecution execution) {

    String baseUrl = webhookUrl.getValue(execution).toString();
    String pathVariable = (String)  execution.getVariable("webhookUrlPath");
    String path = Objects.nonNull(pathVariable) ?  pathVariable : (String) webhookUrlPath.getValue(execution);
    String queryParams = (String) execution.getVariable("webhookUrlQueryParams");
    String query = Objects.nonNull(queryParams)? queryParams : (String) webhookQueryParams.getValue(execution);

    String url = UriComponentsBuilder.fromUriString(baseUrl)
        .path(path != null ? path : "")
        .query(query != null ? query : "")
        .build()
        .toUriString();

    String method = ofNullable(webhookMethod.getValue(execution))
        .orElseThrow(() -> new IllegalArgumentException("webhookMethod argument is required and was not provided"))
        .toString().toUpperCase();

    Object payloadVariable = execution.getVariable("webhookPayload");
    Object payload = Objects.nonNull(payloadVariable) ? payloadVariable : Objects.nonNull(webhookPayload) ? webhookPayload.getValue(execution) : null;

    Object payloadHeader = execution.getVariable("webhookPayloadHeader");
    Map<String, String> headersMap = ObjectUtil.parseJsonObjectString(
        ofNullable(Objects.nonNull(payloadHeader)? payloadHeader : Objects.nonNull(webhookHeaders) ? webhookHeaders.getValue(execution) : null)
            .orElse("").toString()
    );

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      if (!headersMap.isEmpty()) {
        headersMap.forEach(headers::set);
      } else {
        headersMap.put("Authorization", "Bearer" + globalAuthToken);
      }

      log.info("[IgrpWebhookDelegate] Sending {} request to {}", method, url);
      log.debug("[IgrpWebhookDelegate] Payload: {}", payload);

      String responseBody;
      int statusCode;

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

      execution.setVariable("webhookResponseBody", responseBody);

      execution.setVariable("webhookResponseStatusCode", statusCode);

    } catch (Exception e) {
      log.error("[IgrpWebhookDelegate] Error calling webhook {}", url, e);
      execution.setVariable("webhookError", e.getMessage());
      throw new RuntimeException("IgrpWebhookDelegate execution failed", e);
    }
  }

}
