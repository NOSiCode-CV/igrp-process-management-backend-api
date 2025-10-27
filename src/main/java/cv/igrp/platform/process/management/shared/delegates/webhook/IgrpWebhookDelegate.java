package cv.igrp.platform.process.management.shared.delegates.webhook;

import com.google.gson.JsonElement;
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
import org.springframework.web.client.RestClientResponseException;
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
  public Expression webhookPayloadHeader;

  @Override
  public void execute(DelegateExecution execution) {

    String taskId = execution.getCurrentActivityId();
    String processInstanceId = execution.getProcessInstanceId();
    log.info("[IgrpWebhookDelegate] Executing webhook task: {} from process instance: {}", taskId, processInstanceId);
    String baseUrlVariable = (String) execution.getVariable("webhookUrl");
    String baseUrl = Objects.nonNull(baseUrlVariable)? baseUrlVariable: Objects.nonNull(webhookUrl)? webhookUrl.getValue(execution).toString() : null;
    String pathVariable = (String)  execution.getVariable("webhookUrlPath");
    String path = Objects.nonNull(pathVariable) ?  pathVariable : Objects.nonNull(webhookUrlPath) ? (String) webhookUrlPath.getValue(execution): null;
    String queryParams = (String) execution.getVariable("webhookUrlQueryParams");
    String query = Objects.nonNull(queryParams)? queryParams : Objects.nonNull(webhookQueryParams) ? (String) webhookQueryParams.getValue(execution) : null;

    String url = UriComponentsBuilder.fromUriString(baseUrl)
        .path(path != null ? path : "")
        .query(query != null ? query : "")
        .build()
        .toUriString();

    String methodVariable = (String) execution.getVariable("webhookMethod");
    String method = ofNullable(Objects.nonNull(methodVariable)? methodVariable : Objects.nonNull(webhookMethod) ? webhookMethod.getValue(execution) : null)
        .orElseThrow(() -> new IllegalArgumentException("webhookMethod argument is required and was not provided"))
        .toString().toUpperCase();

    Object payloadVariable = execution.getVariable("webhookPayload");
    Object payload = Objects.nonNull(payloadVariable) ? payloadVariable : Objects.nonNull(webhookPayload) ? webhookPayload.getValue(execution) : "";

    Object payloadHeader = execution.getVariable("webhookPayloadHeader");
    Map<String, String> headersMap = ObjectUtil.parseJsonObjectString(
        ofNullable(Objects.nonNull(payloadHeader)? payloadHeader : Objects.nonNull(webhookPayloadHeader) ? webhookPayloadHeader.getValue(execution) : null)
            .orElse("").toString()
    );

    Object responseBody;
    int statusCode;

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      if (!headersMap.isEmpty()) {
        headersMap.forEach(headers::set);
      } else {
        headers.set("Authorization", "Bearer" + globalAuthToken);
      }

      log.info("[IgrpWebhookDelegate] Sending {} request to {}", method, url);
      log.debug("[IgrpWebhookDelegate] Payload: {}", payload);

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

    } catch (RestClientResponseException e) {
      statusCode = e.getStatusCode().value();
      responseBody = e.getResponseBodyAs(String.class);
      log.warn("[IgrpWebhookDelegate] Webhook returned error {}: {}", statusCode, responseBody);
    } catch (Exception e) {
      log.error("[IgrpWebhookDelegate] Error calling webhook {}", url, e);
      execution.setTransientVariable(taskId + "Error", e.getMessage());
      return;
    }

    JsonElement responseBodyElement = ObjectUtil.parseJsonObject(responseBody);
    Object responseBodyParsed = ObjectUtil.toJavaObject(responseBodyElement);

    execution.getEngineServices().getRuntimeService().setVariable(
        processInstanceId,
        taskId + "Data", responseBodyParsed);

    execution.getEngineServices().getRuntimeService().setVariable(
        processInstanceId,taskId + "StatusCode", statusCode);

  }

}
